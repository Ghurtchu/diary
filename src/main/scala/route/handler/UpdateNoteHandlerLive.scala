package route.handler

import model.Note
import route.interface.UpdateNoteService
import route.implementation.UpdateNoteServiceLive

import zhttp.http._
import zio.*
import zio.json.*
import RequestHandlerDefinitions.UpdateNoteHandler

final case class UpdateNoteHandlerLive(updateNoteService: UpdateNoteService) extends UpdateNoteHandler:

  override def handle(request: Request, noteId: Long): Task[Response] = 
    for 
      noteEither <- request.bodyAsString.map(_.fromJson[Note])
      response   <- noteEither.fold (
        _    => ZIO.succeed(Response.text("Invalid Json").setStatus(Status.BadRequest)),
        note => updateNoteService.updateNote(noteId, note).map(_.fold(
          failure => Response.text(failure).setStatus(Status.BadRequest),
          success => Response.text(success).setStatus(Status.NoContent)
        ))
      )
    yield response


object UpdateNoteHandlerLive:
  
  lazy val layer: URLayer[UpdateNoteService, UpdateNoteHandler] = ZLayer.fromFunction(UpdateNoteHandlerLive.apply)
