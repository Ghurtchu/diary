package service.search

import db.{CRUD, NotesRepository}
import model.{Note, User}
import zio.{Task, ZIO}

import java.time.Instant
import java.util.Date

object NotesSearchService extends SearchService[Note] {

  val notesRepository: CRUD[Note] = NotesRepository

  override def searchByTitle(title: String): Task[Option[Note]] = for {
    notes     <- notesRepository.getAll
    maybeNote <- ZIO.succeed(notes.find(_.title == title))
  } yield maybeNote

}
