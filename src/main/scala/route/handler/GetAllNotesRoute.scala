package route.handler

import db.{CRUD, NotesRepository}
import model.Note
import route.interface.CanRetrieveRecords
import route.implementation.GetAllNotesService
import zhttp.http.Response
import zio.*
import zio.json.*
import route.interface.BasicRequestHandler


class GetAllNotesRoute extends BasicRequestHandler {

  private val getAllNotesService: CanRetrieveRecords[Note] = GetAllNotesService()

  final override def handle: Task[Response] = for {
    notes    <- getAllNotesService.retrieveRecords
    response <- ZIO.succeed(Response.text(notes.toJsonPretty))
  } yield response

}
