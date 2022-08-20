package util.search

import db.{CRUD, NotesRepository}
import model.{Note, User}
import route.handler.{Exact, NonExact, SearchCriteria}
import zio.{Task, ZIO}

import java.time.Instant
import java.util.Date

object NotesSearchService extends CanSearch[Note] {

  val notesRepository: CRUD[Note] = NotesRepository()

  override def searchByTitle(title: String, criteria: SearchCriteria): Task[Either[String, List[Note]]] = for {
    notes      <- notesRepository.getAll
    response <-
      criteria match
        case Exact    => ZIO.succeed(notes.find(_.title == title).fold(Left(s"No matches with title $title"))(note => Right(note :: Nil)))
        case NonExact => ZIO.succeed {
          val maybeNotes = notes.filter(_.title.contains(title))
          if maybeNotes.nonEmpty then Right(maybeNotes) else Left(s"No matches with title $title")
    }
  } yield response

}
