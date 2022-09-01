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

  val mongo: UIO[MongoDatabase] = MongoDatabaseProvider.get

  override def getById(id: Int): Task[Option[Note]] = ZIO.attempt(None)

  override def getAll: UIO[List[Note]] = ZIO.succeed(Nil)

  override def update(id: Int, newNote: Note): Task[UpdateStatus] = ZIO.attempt(Left("TODO"))

  override def delete(noteId: Int): Task[DeletionStatus] = ZIO.attempt(Left("TODO"))

  override def add(note: Note): Task[CreationStatus] = for {
    noteWithId     <- ZIO.succeed(note.copy(id = Some(scala.util.Random.nextLong(Long.MaxValue))))
    db             <- mongo
    resultSequence <- ZIO.fromFuture { implicit ec =>
      db.getCollection("notes")
        .insertOne(Document(noteWithId.toJson))
        .toFuture()
    }
    creationStatus <- ZIO.succeed(if resultSequence.wasAcknowledged() then Right("Note has been added") else Left("Note has not been added"))
  } yield creationStatus

  override def getNotesByUserId(userId: Long): UIO[List[Note]] = for {
    db             <- mongo
    resultSequence <- ZIO.fromFuture { implicit ec =>
      db.getCollection("notes")
        .find(equal("userId", userId))
        .toFuture()
    }.orDieWith(identity)
    notes          <- ZIO.succeed {
      resultSequence.map { doc =>
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

  override def getNoteByIdAndUserId(id: Long, userId: Long): UIO[Option[Note]] = ZIO.succeed(None)

  override def deleteNoteByIdAndUserId(noteId: Long, userId: Long): Task[DeletionStatus] = ZIO.attempt(Left("TODO"))

}

object NotesRepository {
  
  lazy val layer: ULayer[NoteCRUD] = ZLayer.fromFunction(NotesRepository.apply _)

}