package route.implementation

import db.{CRUD, NotesRepository}
import model.Note
import route.interface.RecordRetriever
import zhttp.http.Response
import zio.*
import zio.json.*

class GetNoteService(private final val notesRepository: CRUD[Note]) extends RecordRetriever[Note] {

  override def retrieveRecord(id: Int): Task[Either[String, Note]] = for {
    maybeNote <- notesRepository getById id
    response  <- ZIO.succeed(maybeNote.fold(Left(s"Could not find the note with id ${id.withQuotes}"))(Right(_)))
  } yield response
}

object GetNoteService {
  
  def spawn(notesRepository: CRUD[Note]): GetNoteService = new GetNoteService(notesRepository)
  
  def layer: ULayer[GetNoteService] = 
    NotesRepository.layer >>> ZLayer.fromFunction(GetNoteService.spawn)
  
  
}
