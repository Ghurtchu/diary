package db.note

import db.*
import db.DbError.InvalidId
import db.Repository.*
import db.mongo.{DataSource, DatabaseContext, MongoDatabaseInitializer}
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

  override def update(id: Long, newNote: Note): Task[DbOperation] = 
    for
      db           <- mongo
      updateResult <- ZIO.fromFuture { implicit ec =>
        db.getCollection("notes")
          .replaceOne(equal("id", id), Document(newNote.toJson))
          .toFuture()
      }
      updateStatus <- updateResult.fold(updateResult.getModifiedCount == 1, DbSuccess.Updated(s"Note with id $id has been updated"), DbError.InvalidId(s"Note with id $id has not been updated"))
    yield updateStatus

  override def delete(noteId: Long): Task[DbOperation] = 
    for
      db             <- mongo
      deleteResult   <- ZIO.fromFuture { implicit ec =>
        db.getCollection("notes")
          .deleteOne(equal("id", noteId))
          .toFuture()
      }
      deletionStatus <- deleteResult.fold(deleteResult.getDeletedCount == 1, DbSuccess.Deleted(s"Note with id $noteId has been deleted"), DbError.InvalidId(s"Could not delete Note. Note with id: $noteId does not exist"))
    yield deletionStatus

  override def add(note: Note): Task[DbOperation] = 
    for
      db              <- mongo
      noteWithId      <- ZIO.succeed(note.copy(id = Some(scala.util.Random.nextLong(Long.MaxValue))))
      insertResult    <- ZIO.fromFuture { implicit ec =>
        db.getCollection("notes")
          .insertOne(Document(noteWithId.toJson))
          .toFuture()
      }
      insertionStatus <- insertResult.fold(insertResult.wasAcknowledged, DbSuccess.Created("Note has been created"), DbError.ReasonUnknown("Note has not been added"))
    yield insertionStatus

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

  override def deleteNoteByIdAndUserId(noteId: Long, userId: Long): Task[DbOperation] = 
    for
      db             <- mongo
      deleteResult   <- ZIO.fromFuture { implicit ec =>
        db.getCollection("notes")
          .deleteOne(and(equal("id", noteId), equal("userId", userId)))
          .toFuture()
      }
      deletionStatus <- deleteResult.fold(deleteResult.getDeletedCount == 1, DbSuccess.Deleted(s"Note with id '$noteId' has been deleted"), DbError.InvalidId(s"Combination of userId and noteId is wrong, could not delete the note"))
    yield deletionStatus

  private def parseDocumentToNote(document: Document): Option[Note] = 
    Option(document).fold(None)(doc => Some(buildNoteWithoutUserId(doc)))

  private def buildFullNote(doc: Document): Note =
    Note(
      id        = doc("id").asInt64.getValue,
      title     = doc("title").asString.getValue,
      body      = doc("body").asString.getValue,
      createdAt = doc("createdAt").asString.getValue,
      userId    = doc("userId").asInt64.getValue
    )

  private def buildNoteWithoutUserId(doc: Document): Note =
    Note(
      id        = doc("id").asInt64.getValue,
      title     = doc("title").asString.getValue,
      body      = doc("body").asString.getValue,
      createdAt = doc("createdAt").asString.getValue
    )

  private def parseDocumentsToNoteList(documents: Seq[Document]): List[Note] = documents.map(parseDocumentToNote).toList.flatten

object NotesRepositoryLive:
  
  lazy val layer: URLayer[DataSource, NotesRepository] = ZLayer.fromFunction(NotesRepositoryLive.apply)