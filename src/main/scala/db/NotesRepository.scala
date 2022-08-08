package db

import model.{Note, User}
import zio._

import scala.util.Random
import java.time.Instant
import java.util.Date

object NotesRepository extends CRUD[Note] {

  override def getById(id: Int): Task[Option[Note]] = ZIO.attempt {
    Some(Note(
      1,
      "first note",
      "note body",
      Date.from(Instant.now()).toString,
      User(1, "Nika", "Ghurtchumelia")))
  }

  override def getAll: UIO[List[Note]] = ZIO.succeed {
    Note(1, "title", "body", Date.from(Instant.now()).toString, User(1, "Nika", "Ghurtchumelia"))
      :: (2 to 11).toList.map(i => Note(i, Random.nextString(15), Random.nextString(100), Date.from(Instant.now()).toString, User(i, Random.nextString(10), Random.nextString(10))))
  }

  override def update(id: Int, a: Note): Task[Boolean] = ZIO.attempt(true)

  override def delete(id: Int): Task[Boolean] = ZIO.attempt(true)

  override def add(a: Note): Task[Boolean] = ZIO.attempt(true)
}
