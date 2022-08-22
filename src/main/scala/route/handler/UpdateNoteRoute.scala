package route.handler

import model.Note
import route.interface.RecordUpdater
import route.implementation.UpdateNoteService

import zhttp.http._
import zio.*
import zio.json.*
import route.interface.AdvancedRequestHandler

class UpdateNoteRoute extends AdvancedRequestHandler[Request, Int] {

  private val updateNoteService: RecordUpdater[Note] = new UpdateNoteService()

  def handle(request: Request, id: Int): Task[Response] = for {
    noteAsJson   <- request.bodyAsString
    noteEither   <- ZIO.succeed(noteAsJson.fromJson[Note])
    updateStatus <- noteEither.fold(err => ZIO.fail(new RuntimeException(err)), note => updateNoteService.updateRecord(id, note))
    response     <- updateStatus.fold(
        err    => ZIO.succeed(Response.text(err)),
        status => ZIO.succeed(Response.text(status)))
  } yield response

}
