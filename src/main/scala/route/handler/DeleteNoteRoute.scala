package route.handler

import db.NotesRepository
import route.interface.RecordRemover
import route.implementation.DeleteNoteService
import zhttp.http.Response
import zio.*

class DeleteNoteRoute {

  val deleteNoteService: RecordRemover = DeleteNoteService()

  def handle(id: Int): Task[Response] =
    deleteNoteService.deleteRecord(id)
      .map(_.fold(err => Response text err, succ => Response.text(succ)))

}
