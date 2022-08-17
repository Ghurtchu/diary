package db

import model.{Note, User}
import zio.*
import CRUD._

import scala.util.Random
import java.time.Instant
import java.util.Date
import scala.collection.mutable.ListBuffer

class NotesRepository extends CRUD[Note] {
  
  val inMemoryDB: InMemoryDB.type = InMemoryDB

  override def getById(id: Int): Task[Option[Note]] = ZIO.attempt(inMemoryDB.notes.find(note => note.id.isDefined && note.id.get == id))

  override def getAll: UIO[List[Note]] = ZIO.succeed(inMemoryDB.notes.toList)

  override def update(id: Int, newNote: Note): Task[UpdateStatus] = for {
    notes        <- ZIO.succeed(inMemoryDB.notes)
    maybeNote    <- ZIO.succeed(notes.find(_.id.get == id))
    index        <- ZIO.succeed(maybeNote.fold(-1)(notes.indexOf(_)))
    _            <- ZIO.succeed(inMemoryDB.notes.update(index, newNote))
    updateStatus <- ZIO.succeed {
      if inMemoryDB.notes.contains(newNote) then Right("Update was successful")
      else Left("Update was unsuccessful")
    }
  } yield updateStatus

  override def delete(noteId: Int): Task[DeletionStatus] = for {
    notes        <- ZIO.succeed(InMemoryDB.notes)
    maybeNote    <- ZIO.succeed(notes.find(note => note.id.isDefined && note.id.get == noteId))
    deleteStatus <- ZIO.attempt {
      maybeNote.fold(Left(s"Record with id $noteId does not exist")) { note =>
        notes subtractOne note
        
        if !(notes contains note) then Right("Record has been deleted") else Left("Record has not been deleted")
      }
    }
  } yield deleteStatus

  override def add(note: Note): Task[CreationStatus] = for {
    creationStatus <- ZIO.succeed {
      if inMemoryDB.notes.contains(note) then Left("Note already exists")
      else {
        inMemoryDB.notes.addOne(note)
        if inMemoryDB.notes.contains(note) then Right("Note has been added")
        else Left("Note has not been added")
      }
    }
  } yield creationStatus
}
