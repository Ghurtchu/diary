package route.handler

import db.NotesRepository
import route.implementation
import route.interface.RecordRemover
import route.implementation.DeleteNoteService
import zhttp.http.Response
import zio.*

class DeleteNoteRoute {

  def handle(id: Int): RIO[DeleteNoteService, Response] = for {
    service      <- ZIO.service[DeleteNoteService]
    deleteStatus <- service.deleteRecord(id)
    response     <- ZIO.succeed(deleteStatus.fold(Response.text, Response.text))
  } yield response

}
