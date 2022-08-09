package db

import model.{Note, User}
import zio.*

import scala.util.Random
import java.time.Instant
import java.util.Date
import scala.collection.mutable.ListBuffer

object NotesRepository extends CRUD[Note] {

  val inMemoryDB: InMemoryDB.type = InMemoryDB

  override def getById(id: Int): Task[Option[Note]] = ZIO.attempt(inMemoryDB.notes.find(_.id == id))

  override def getAll: UIO[List[Note]] = ZIO.succeed(inMemoryDB.notes.toList)

  override def update(id: Int, newNote: Note): Task[Boolean] = for {
    notes                   <- ZIO.succeed(inMemoryDB.notes)
    maybeNote               <- ZIO.succeed(notes.find(_.id == id))
    index                   <- ZIO.succeed(maybeNote.fold(-1)(notes.indexOf(_)))
    _                       <- ZIO.succeed(inMemoryDB.notes.update(index, newNote))
    updateStatus            <- ZIO.succeed(inMemoryDB.notes.contains(newNote))
  } yield updateStatus

  override def delete(id: Int): Task[Boolean] = for {
    index        <- ZIO.succeed(inMemoryDB.notes.map(_.id).indexOf(id))
    note         <- ZIO.succeed(inMemoryDB.notes.remove(index))
    deleteStatus <- ZIO.succeed(!inMemoryDB.notes.contains(note))
  } yield deleteStatus

  override def add(note: Note): Task[Boolean] = for {
    _      <- ZIO.succeed(inMemoryDB.notes.addOne(note))
    status <- ZIO.succeed(inMemoryDB.notes.contains(note))
  } yield status
}
