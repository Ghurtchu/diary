package service.search

import db.{CRUD, NotesRepository}
import model.{Note, User}
import zio.{Task, ZIO}

import java.time.Instant
import java.util.Date

object NotesSearchService extends CanSearch[Note] {

  val notesRepository: CRUD[Note] = NotesRepository()

  override def searchByTitle(title: String): Task[Either[String, Note]] = for {
    notes     <- notesRepository.getAll
    maybeNote <- ZIO.succeed(notes.find(_.title == title))
    response  <- ZIO.succeed(maybeNote.fold(Left(s"Could not find Note with title $title"))(Right(_)))
  } yield response

}
