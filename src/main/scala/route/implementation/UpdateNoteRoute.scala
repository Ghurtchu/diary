package route.implementation

import model.Note
import route.interface.CanUpdateRecord
import route.service.UpdateNoteService

import zhttp.http._
import zio.*
import zio.json.*

class UpdateNoteRoute {

  private val updateNoteService: CanUpdateRecord[Note] = new UpdateNoteService()

  def handle(request: Request, id: Int): Task[Response] = for {
    noteAsJson   <- request.bodyAsString
    noteEither   <- ZIO.succeed(noteAsJson.fromJson[Note])
    updateStatus <- noteEither.fold(err => ZIO.fail(new RuntimeException(err)), note => updateNoteService.updateRecord(id, note))
    response     <- updateStatus.fold(
        err    => ZIO.succeed(Response.text(err)),
        status => ZIO.succeed(Response.text(status)))
  } yield response

}
