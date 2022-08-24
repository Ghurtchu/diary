package route.handler

import model.Note
import route.interface.RecordUpdater
import route.implementation.UpdateNoteService

import zhttp.http._
import zio.*
import zio.json.*
import route.interface.ComplexRequestHandler

class UpdateNoteRoute extends ComplexRequestHandler[Request, Int, RecordUpdater[Note]] {

  final override def handle(request: Request, id: Int): RIO[RecordUpdater[Note], Response] = for {
    service      <- ZIO.service[RecordUpdater[Note]]
    noteAsJson   <- request.bodyAsString
    noteEither   <- ZIO.succeed(noteAsJson.fromJson[Note])
    updateStatus <- noteEither.fold(err => ZIO.fail(new RuntimeException(err)), note => service.updateRecord(id, note))
    response     <- updateStatus.fold(
        err    => ZIO.succeed(Response.text(err)),
        status => ZIO.succeed(Response.text(status)))
  } yield response

}
