package util.search

import db.{CRUD, NotesRepository}
import model.{Note, User}
import route.handler.{Exact, NonExact, SearchCriteria}
import zio.{Task, ZIO, ZLayer}

import java.time.Instant
import java.util.Date

class SearchNoteService(notesRepository: CRUD[Note]) extends SearchService[Note] {

  final override def searchByTitle(title: String, criteria: SearchCriteria): Task[Either[String, List[Note]]] = for {
    notes      <- notesRepository.getAll
    response   <-
      criteria match {
        case Exact    => ZIO.succeed(notes.find(_.title == title).fold(Left(s"No matches with title $title"))(note => Right(note :: Nil)))
        case NonExact => ZIO.succeed {
          val maybeNotes = notes.filter(note => note.title.replace(" ", "").toLowerCase.contains(title.replace(" ","").toLowerCase))
          if maybeNotes.nonEmpty then Right(maybeNotes) else Left(s"No matches with title $title")
        }
      }
  } yield response

}

object SearchNoteService {

  def spawn(notesRepository: CRUD[Note]): SearchNoteService = new SearchNoteService(notesRepository)

  def layer: ZLayer[Any, Nothing, SearchNoteService] = NotesRepository.layer >>> ZLayer.fromFunction(SearchNoteService.spawn)

}
