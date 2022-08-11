package route.implementation

import db.NotesRepository
import route.interface.CanDeleteRecord
import service.route.DeleteNoteService
import zhttp.http.Response
import zio.*

class DeleteNoteRoute {

  val deleteNoteService: CanDeleteRecord = DeleteNoteService()

  def handle(id: Int): Task[Response] =
    deleteNoteService.serve(id)
      .map(_.toResponse)

}
