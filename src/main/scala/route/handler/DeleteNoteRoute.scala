package route.handler

import db.NotesRepository
import route.interface.CanDeleteRecord
import route.implementation.DeleteNoteService
import zhttp.http.Response
import zio.*
import route.interface.CommonRequestHandler

class DeleteNoteRoute extends CommonRequestHandler[Int] {

  val deleteNoteService: CanDeleteRecord = DeleteNoteService()

  def handle(id: Int): Task[Response] =
    deleteNoteService.deleteRecord(id)
      .map(_.fold(err => Response text err, succ => Response.text(succ)))

}
