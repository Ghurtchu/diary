package route.handler

import model.Note
import route.interface.CanRetrieveRecord
import route.service.GetNoteService
import zhttp.http.Response
import zio.*
import zio.json.*

class GetNoteRoute {

  private val getNoteService: CanRetrieveRecord[Note] = GetNoteService()

  def handle(id: Int): Task[Response] = getNoteService.retrieveRecord(id)
    .map(_.fold(_.toNotFoundResponse, toJsonResponse))

}
