package route.handler

import model.Note
import route.interface.RecordUpdater
import route.implementation.UpdateNoteService

import zhttp.http._
import zio.*
import zio.json.*
import route.interface.ComplexRequestHandler
import UpdateNoteRoute.NoteID

object UpdateNoteRoute {
  type NoteID = Int
}

class UpdateNoteRoute extends ComplexRequestHandler[Request, NoteID, RecordUpdater[Note]] {

  final override def handle(request: Request, noteId: NoteID): RIO[RecordUpdater[Note], Response] = for {
    updateNoteService <- ZIO.service[RecordUpdater[Note]]
    noteAsJson        <- request.bodyAsString
    noteEither        <- ZIO.succeed(noteAsJson.fromJson[Note])
    updateStatus      <- noteEither.fold(err => ZIO.fail(new RuntimeException(err)), note => updateNoteService.updateRecord(noteId, note))
    response          <- updateStatus.fold(
        err    => ZIO.succeed(Response.text(err)),
        status => ZIO.succeed(Response.text(status)))
  } yield response

}
