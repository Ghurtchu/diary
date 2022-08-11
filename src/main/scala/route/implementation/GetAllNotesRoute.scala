package route.implementation

import db.{CRUD, NotesRepository}
import model.Note
import route.interface.CanRetrieveCollection
import route.service.GetAllNotesService
import zhttp.http.Response
import zio.*
import zio.json.*


class GetAllNotesRoute {

  private val getAllNotesService: CanRetrieveCollection[Note] = GetAllNotesService()

  def handle: UIO[Response] = for {
    notes    <- getAllNotesService.serve
    response <- ZIO.succeed(Response.text(notes.toJsonPretty))
  } yield response

}
