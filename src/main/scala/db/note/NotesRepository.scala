package db.note

import org.mongodb.scala.Document
import db.CRUD.{CreationStatus, DeletionStatus, UpdateStatus}
import db.InMemoryDB
import db.mongo.{MongoDatabaseBuilder, MongoDatabaseProvider}
import db.note.NoteCRUD
import model.{Note, User}
import zio.*
import zio.json.*
import model.Note.*

import java.time.Instant
import java.util.Date
import scala.collection.mutable.ListBuffer
import scala.util.Random
import org.mongodb.scala._
import org.mongodb.scala.model.Filters._


final case class NotesRepository() extends NoteCRUD {

  private final lazy val mongo: UIO[MongoDatabase] = MongoDatabaseProvider.get

  override def getById(id: Long): Task[Option[Note]] = for {
    db        <- mongo
    document  <- ZIO.fromFuture { implicit ec =>
      db.getCollection("notes")
        .find(equal("id", id))
        .first()
        .toFuture()
    }
    maybeNote <- ZIO.succeed(parseDocumentToNote(document))
  } yield maybeNote

  override def getAll: Task[List[Note]] = for {
    db        <- mongo
    documents <- ZIO.fromFuture { implicit ec =>
      db.getCollection("notes")
        .find()
        .toFuture()
    }
    notes     <- ZIO.succeed(parseDocumentsToNoteList(documents))
  } yield notes

  override def update(id: Long, newNote: Note): Task[UpdateStatus] = for {
    db          <- mongo
    queryResult <- ZIO.fromFuture { implicit ec =>
      db.getCollection("notes")
        .updateOne(equal("id", id), Document(newNote.toJson))
        .toFuture()
    }
    updateStatus <- ZIO.succeed {
      if queryResult.wasAcknowledged then Right(s"Note with id '$id' has been updated successfully")
      else Left(s"Note with id '$id' has not been updated'")
    }
  } yield updateStatus

  override def delete(noteId: Long): Task[DeletionStatus] = for {
    db             <- mongo
    queryResult    <- ZIO.fromFuture { implicit ec =>
      db.getCollection("notes")
        .deleteOne(equal("id", noteId))
        .toFuture()
    }
    deletionStatus <- ZIO.succeed{
      if queryResult.wasAcknowledged then Right(s"Note with id '$noteId' has been deleted")
      else Left(s"Note with id '$noteId' has not been deleted")
    }
  } yield deletionStatus

  override def add(note: Note): Task[CreationStatus] = for {
    noteWithId     <- ZIO.succeed(note.copy(id = Some(scala.util.Random.nextLong(Long.MaxValue))))
    db             <- mongo
    queryResult    <- ZIO.fromFuture { implicit ec =>
      db.getCollection("notes")
        .insertOne(Document(noteWithId.toJson))
        .toFuture()
    }
    creationStatus <- ZIO.succeed(if queryResult.wasAcknowledged() then Right("Note has been added") else Left("Note has not been added"))
  } yield creationStatus

  override def getNotesByUserId(userId: Long): Task[List[Note]] = for {
    db        <- mongo
    documents <- ZIO.fromFuture { implicit ec =>
      db.getCollection("notes")
        .find(equal("userId", userId))
        .toFuture()
    }
    notes     <- ZIO.succeed(parseDocumentsToNoteList(documents))
  } yield notes

  override def getNoteByIdAndUserId(id: Long, userId: Long): Task[Option[Note]] = for {
    db       <- mongo
    document <- ZIO.fromFuture { implicit ec =>
      db.getCollection("notes")
        .find(and(equal("id", id), equal("userId", userId)))
        .first()
        .toFuture()
    }
    note     <- ZIO.attempt(parseDocumentToNote(document))
  } yield note

  override def deleteNoteByIdAndUserId(noteId: Long, userId: Long): Task[DeletionStatus] = ZIO.attempt(Left("TODO"))

  private def parseDocumentToNote(document: Document): Option[Note] = {
    Option(document).fold(None) { doc =>
      Some(
        Note(
          id        = doc("id").asInt64.getValue,
          title     = doc("title").asString.getValue,
          body      = doc("body").asString.getValue,
          createdAt = doc("createdAt").asString.getValue,
          userId    = doc("userId").asInt64.getValue
        )
      )
    }
  }

  private def parseDocumentsToNoteList(documents: Seq[Document]) = documents.map(parseDocumentToNote).toList.flatten

}

object NotesRepository {
  
  lazy val layer: ULayer[NoteCRUD] = ZLayer.fromFunction(NotesRepository.apply _)

}