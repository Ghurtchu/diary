package route.implementation

import route.interface.CanRetrieveResponse
import service.NotesService
import zhttp.http.Response
import zio.*
import zio.json.*

class GetAllNotesRoute extends CanRetrieveResponse {

  private val notesService: NotesService.type = NotesService

  override def handle: UIO[Response] = for {
    notes    <- notesService.getAll
    response <- ZIO.succeed(Response.text(notes.toJsonPretty))
  } yield response

}
