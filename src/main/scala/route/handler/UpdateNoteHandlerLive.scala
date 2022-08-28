package route.handler

import model.Note
import route.interface.RecordUpdater
import route.implementation.UpdateNoteService

import zhttp.http._
import zio.*
import zio.json.*
import UpdateNoteHandlerLive.NoteID

trait UpdateNoteHandler {
  def handle(request: Request, noteId: NoteID): Task[Response]
}

final case class UpdateNoteHandlerLive(updateNoteService: RecordUpdater[Note]) extends UpdateNoteHandler {

  final override def handle(request: Request, noteId: NoteID): Task[Response] = for {
    noteAsJson        <- request.bodyAsString
    noteEither        <- ZIO.succeed(noteAsJson.fromJson[Note])
    updateStatus      <- noteEither.fold(err => ZIO.fail(new RuntimeException(err)), note => updateNoteService.updateRecord(noteId, note))
    response          <- updateStatus.fold(
        err    => ZIO.succeed(Response.text(err)),
        status => ZIO.succeed(Response.text(status)))
  } yield response

}

object UpdateNoteHandlerLive {
  
  type NoteID = Int
  
  lazy val layer: URLayer[RecordUpdater[Note], UpdateNoteHandler] = ZLayer.fromFunction(UpdateNoteHandlerLive.apply _)
  
}
