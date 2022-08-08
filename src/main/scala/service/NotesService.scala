package service

import model._
import zio._

import java.time.Instant
import java.util.Date

object NotesService extends CRUD[Note]:

  override def add(note: Note): Task[Boolean] = ZIO.attempt {
    true
  }

  override def getAll: UIO[List[Note]] = ZIO.succeed {
    List()
  }

  override def getById(id: Int): Task[Option[Note]] = ZIO.attempt {
    Some(Note(
      1,
      "first note",
      "note body",
      Date.from(Instant.now()).toString,
      User(1, "Nika", "Ghurtchumelia")))
  }

  override def delete(id: Int): Task[Boolean] = ZIO.attempt {
    true
  }

  override def update(id: Int, a: Note): Task[Boolean] = ZIO.attempt {
    true
  }

