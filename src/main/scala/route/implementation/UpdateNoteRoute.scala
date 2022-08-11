package route.implementation

import model.Note
import route.interface.CanUpdateRecord
import service.route.UpdateNoteService

import zhttp.http.Response
import zio.*
import zio.json.*

class UpdateNoteRoute {

  private val updateNoteService: CanUpdateRecord = new UpdateNoteService()

  def handle(id: Int, recordAsJson: String): Task[Response] =
    updateNoteService.serve(id, recordAsJson)
      .map(_.toResponse)

}
