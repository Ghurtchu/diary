package db

import model.{Note, User}
import zio.*
import CRUD._
import zio.ZLayer
import scala.util.Random
import java.time.Instant
import java.util.Date
import scala.collection.mutable.ListBuffer

final case class NotesRepository() extends NoteCRUD {
  
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
    noteId         <- ZIO.succeed(scala.util.Random.nextInt(Int.MaxValue))
    creationStatus <- ZIO.succeed {
      if inMemoryDB.notes.contains(note) then Left("Note already exists")
      else {
        val noteWithId = note.copy(id = Some(noteId))
        inMemoryDB.notes.addOne(noteWithId)
        if inMemoryDB.notes.contains(noteWithId) then Right("Note has been added")
        else Left("Note has not been added")
      }
    }
  } yield creationStatus

  override def getNotesByUserId(userId: Int): UIO[List[Note]] = ZIO.succeed {
    inMemoryDB.notes
      .filter { note =>
        note.userId.isDefined
          && note.userId.get == userId
      }.toList
  }

  override def getNoteByIdAndUserId(id: Int, userId: Int): UIO[Option[Note]] = ZIO.succeed{
    inMemoryDB.notes
      .find { note =>
        note.id.isDefined
          && note.userId.isDefined
          && note.id.get == id
          && note.userId.get == userId
      }
  }

  override def deleteNoteByIdAndUserId(noteId: Int, userId: Int): Task[DeletionStatus] = for {
    notes        <- ZIO.succeed(InMemoryDB.notes)
    maybeNote    <- ZIO.succeed(notes.find { note => 
      note.id.isDefined && 
        note.id.get == noteId && 
        note.userId.isDefined &&
        note.userId.get == userId
    })
    deleteStatus <- ZIO.attempt {
      maybeNote.fold(Left(s"Record with id $noteId does not exist")) { note =>
        notes subtractOne note

        if !(notes contains note) then Right("Record has been deleted") else Left("Record has not been deleted")
      }
    }
  } yield deleteStatus


}

object NotesRepository {
  
  lazy val layer: ULayer[NoteCRUD] = ZLayer.fromFunction(NotesRepository.apply _)

}