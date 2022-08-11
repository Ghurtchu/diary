package route.implementation

import model.Note
import route.interface.CanCreateRecord
import service.route.CreateNoteService
import zhttp.http.Response
import zio.*
import zio.json.*

import java.net.http.HttpResponse.ResponseInfo

class CreateNoteRoute(private val noteAsJson: String) {

  private val createNoteRouteService: CanCreateRecord = new CreateNoteService()

  def handle: Task[Response] =
    createNoteRouteService.serve(noteAsJson)
      .map(_.toResponse)

}
