package route.implementation

import db.{CRUD, NotesRepository}
import model.Note
import route.interface.RecordRetriever
import zhttp.http.Response
import zio.*
import zio.json.*

final case class GetNoteService(private final val notesRepository: CRUD[Note]) extends RecordRetriever[Note] {

  override def retrieveRecord(id: Int): Task[Either[String, Note]] = for {
    maybeNote <- notesRepository getById id
    response  <- ZIO.succeed(maybeNote.fold(Left(s"Could not find the note with id ${id.withQuotes}"))(Right(_)))
  } yield response
}

object GetNoteService {
  
  lazy val layer: URLayer[CRUD[Note], RecordRetriever[Note]] = 
    ZLayer.fromFunction(GetNoteService.apply _)
  
}
