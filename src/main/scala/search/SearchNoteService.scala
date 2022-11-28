package search

import db.note.{NotesRepository, NotesRepositoryLive}
import domain.Domain.Note
import zio.{Task, UIO, ZIO, ZLayer}

import java.time.Instant
import java.util.Date

final case class SearchNoteService(notesRepository: NotesRepository) extends SearchService[Note]:

  override def searchByTitle(title: String, searchCriteria: SearchCriteria, userId: Long): Task[Either[String, List[Note]]] =
    for 
      notes    <- notesRepository getNotesByUserId userId
      response <- searchCriteria.fold(getExactMatches(title, notes))(getNonExactMatches(title, notes))
    yield response

  private def getNonExactMatches(title: String, notes: List[Note]): UIO[Either[String, List[Note]]] = ZIO.succeed {
      val maybeNotes = notes.filter(note => note.title.replace(" ", "").toLowerCase.contains(title.replace(" ", "").toLowerCase))
      if maybeNotes.nonEmpty then Right(maybeNotes) else Left(s"No matches with title $title")
    }

  private def getExactMatches(title: String, notes: List[Note]): UIO[Either[String, List[Note]]] =
    ZIO.succeed(notes.find(_.title == title).fold(Left(s"No matches with title $title"))(note => Right(note :: Nil)))
  


object SearchNoteService:

  lazy val layer: ZLayer[NotesRepository, Nothing, SearchService[Note]] = ZLayer.fromFunction(SearchNoteService.apply)

