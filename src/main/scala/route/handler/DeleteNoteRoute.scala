package route.handler

import db.NotesRepository
import route.implementation
import route.interface._
import route.implementation.DeleteNoteService
import zhttp.http.Response
import zio.*

class DeleteNoteRoute extends CommonRequestHandler[Int, RecordRemover] {

  final override def handle(id: Int): RIO[RecordRemover, Response] = for {
    recordRemoverService <- ZIO.service[RecordRemover]
    deleteStatus         <- recordRemoverService.deleteRecord(id)
    response             <- ZIO.succeed(deleteStatus.fold(Response.text, Response.text))
  } yield response

}
