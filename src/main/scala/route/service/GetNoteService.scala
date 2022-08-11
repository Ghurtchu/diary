package route.service

import db.{CRUD, NotesRepository}
import model.Note
import route.interface.CanRetrieveRecord
import zhttp.http.Response
import zio.*
import zio.json.*

class GetNoteService extends CanRetrieveRecord[Note] {

  private val notesRepository: CRUD[Note] = NotesRepository

  override def serve(id: Int): Task[Either[String, Note]] = for {
    maybeNote <- notesRepository getById id
    response  <- ZIO.succeed(maybeNote.fold(Left(s"Could not find the note with id $id"))(Right(_)))
  } yield response
}
