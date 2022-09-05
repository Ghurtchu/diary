package db

import db.Repository.*
import db.mongo.{DataSource, DatabaseContext, MongoDatabaseInitializer}
import db.*
import db.DbError.InvalidId
import model.Note.*
import model.{Note, User}
import org.mongodb.scala.*
import org.mongodb.scala.model.Filters.*
import org.mongodb.scala.result.{DeleteResult, InsertOneResult}
import zio.*
import zio.json.*

import java.time.Instant
import java.util.Date
import scala.collection.mutable.ListBuffer
import scala.util.Random

final case class NotesRepositoryLive(dataSource: DataSource) extends NotesRepository:

  private final val mongo: UIO[MongoDatabase] = dataSource.getCtx.map(_.mongoDatabase.get)

  override def getById(id: Long): Task[Option[Note]] = 
    for
      db        <- mongo
      document  <- ZIO.fromFuture { implicit ec =>
        db.getCollection("notes")
          .find(equal("id", id))
          .first()
          .toFuture()
      }
      maybeNote <- ZIO.attempt(parseDocumentToNote(document))
    yield maybeNote

  override def getAll: Task[List[Note]] = 
    for
      db        <- mongo
      documents <- ZIO.fromFuture { implicit ec =>
        db.getCollection("notes")
          .find()
          .toFuture()
      }
      notes     <- ZIO.attempt(parseDocumentsToNoteList(documents))
    yield notes

  override def update(id: Long, newNote: Note): Task[Either[InvalidId, String]] = 
    for
      db           <- mongo
      queryResult  <- ZIO.fromFuture { implicit ec =>
        db.getCollection("notes")
          .replaceOne(equal("id", id), Document(newNote.toJson))
          .toFuture()
      }
      updateStatus <- ZIO.succeed(if queryResult.getModifiedCount != 1 then Left(InvalidId(s"Could not update Note. Note with id: $id does not exist")) else Right(s"Note with id $id has been updated"))
    yield updateStatus

  override def delete(noteId: Long): Task[Either[DbError, String]] = 
    for
      db             <- mongo
      queryRes       <- ZIO.fromFuture { implicit ec =>
        db.getCollection("notes")
          .deleteOne(equal("id", noteId))
          .toFuture()
      }
      deletionStatus <- queryRes.fold(queryRes.getDeletedCount == 1, s"Note with id $noteId has been deleted", InvalidId(s"Could not delete Note. Note with id: $noteId does not exist"))
    yield deletionStatus

  override def add(note: Note): Task[CreationStatus] = 
    for
      db             <- mongo
      noteWithId     <- ZIO.succeed(note.copy(id = Some(scala.util.Random.nextLong(Long.MaxValue))))
      queryResult    <- ZIO.fromFuture { implicit ec =>
        db.getCollection("notes")
          .insertOne(Document(noteWithId.toJson))
          .toFuture()
      }
      creationStatus <- ZIO.succeed(if queryResult.wasAcknowledged then Right("Note has been added successfully") else Left("Note has not been added"))
    yield creationStatus

  override def getNotesByUserId(userId: Long): Task[List[Note]] = 
    for
      db        <- mongo
      documents <- ZIO.fromFuture { implicit ec =>
        db.getCollection("notes")
          .find(equal("userId", userId))
          .toFuture()
      }
      notes     <- ZIO.succeed(parseDocumentsToNoteList(documents))
    yield notes

  override def getNoteByIdAndUserId(id: Long, userId: Long): Task[Option[Note]] = 
    for
      db       <- mongo
      document <- ZIO.fromFuture { implicit ec =>
        db.getCollection("notes")
          .find(and(equal("id", id), equal("userId", userId)))
          .first()
          .toFuture()
      }
      note     <- ZIO.attempt(parseDocumentToNote(document))
    yield note

  override def deleteNoteByIdAndUserId(noteId: Long, userId: Long): Task[Either[DbError, String]] = 
    for 
      db             <- mongo
      queryResult    <- ZIO.fromFuture { implicit ec =>
        db.getCollection("notes")
          .deleteOne(and(equal("id", noteId), equal("userId", userId)))
          .toFuture()
      }
      deletionStatus <- queryResult.fold(queryResult.getDeletedCount == 1, s"Note with id '$noteId' has been deleted", InvalidId(s"Combination of userId and noteId is wrong, could not delete the note"))
    yield deletionStatus

  private def parseDocumentToNote(document: Document): Option[Note] = 
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

  private def parseDocumentsToNoteList(documents: Seq[Document]): List[Note] = documents.map(parseDocumentToNote).toList.flatten

object NotesRepositoryLive:
  
  lazy val layer: URLayer[DataSource, NotesRepository] = ZLayer.fromFunction(NotesRepositoryLive.apply)