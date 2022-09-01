package db.note

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
    db          <- mongo
    queryResult <- ZIO.fromFuture { implicit ec =>
      db.getCollection("notes")
        .find(equal("id", s"Long($id)"))
        .first()
        .toFuture()
    }
    maybeNote   <- ZIO.succeed {
      Option(queryResult).fold(None){ doc =>
        Some(
          Note(
            id        = id,
            title     = doc("title").asString.getValue,
            body      = doc("body").asString.getValue,
            createdAt = doc("createdAt").asString.getValue,
            userId    = doc("userId").asInt64.getValue
         )
        )
      }
    }
  } yield maybeNote

  override def getAll: Task[List[Note]] = for {
    db          <- mongo
    queryResult <- ZIO.fromFuture { implicit ec =>
      db.getCollection("notes")
        .find()
        .toFuture()
    }
    notes       <- ZIO.succeed {
      queryResult.map { doc =>
        Note(
          id        = doc("id").asInt64.getValue,
          title     = doc("title").asString.getValue,
          body      = doc("body").asString.getValue,
          createdAt = doc("createdAt").asString.getValue,
          userId    = doc("userId").asInt64.getValue
        )
      }.toList
    }
  } yield notes

  override def update(id: Long, newNote: Note): Task[UpdateStatus] = ZIO.attempt(Left("TODO"))

  override def delete(noteId: Long): Task[DeletionStatus] = ZIO.attempt(Left("TODO"))

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
    db          <- mongo
    queryResult <- ZIO.fromFuture { implicit ec =>
      db.getCollection("notes")
        .find(equal("userId", userId))
        .toFuture()
    }
    notes       <- ZIO.succeed {
      queryResult.map { doc =>
        Note(
          id        = doc("id").asInt64.getValue,
          title     = doc("title").asString.getValue,
          body      = doc("body").asString.getValue,
          createdAt = doc("createdAt").asString.getValue,
          userId    = userId
        )
      }.toList
    }
  } yield notes

  override def getNoteByIdAndUserId(id: Long, userId: Long): Task[Option[Note]] = for {
    db          <- mongo
    queryResult <- ZIO.fromFuture { implicit ec =>
      db.getCollection("notes")
        .find(
          and(
            equal("id", id),
            equal("userId", userId)
          )
        )
        .first()
        .toFuture()
    }
    notes <- ZIO.succeed {
      Option(queryResult).fold(None) { doc =>
        Some(
          Note(
          id        = doc("id").asInt64.getValue,
          title     = doc("title").asString.getValue,
          body      = doc("body").asString.getValue,
          createdAt = doc("createdAt").asString.getValue,
          userId    = userId
         )
        )
      }
    }
  } yield notes

  override def deleteNoteByIdAndUserId(noteId: Long, userId: Long): Task[DeletionStatus] = ZIO.attempt(Left("TODO"))

}

object NotesRepository {
  
  lazy val layer: ULayer[NoteCRUD] = ZLayer.fromFunction(NotesRepository.apply _)

}