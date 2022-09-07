package route.handler

import model.Note
import route.interface.UpdateNoteService
import route.implementation.UpdateNoteServiceLive

import zhttp.http._
import zio.*
import zio.json.*

trait UpdateNoteHandler:
  def handle(request: Request, noteId: Long): Task[Response]


final case class UpdateNoteHandlerLive(updateNoteService: UpdateNoteService) extends UpdateNoteHandler:

  final override def handle(request: Request, noteId: Long): Task[Response] = 
    for 
      noteEither   <- request.bodyAsString.map(_.fromJson[Note])
      updateStatus <- noteEither.fold(err => ZIO.fail(new RuntimeException(err)), note => updateNoteService.updateNote(noteId, note))
      response     <- ZIO.succeed(updateStatus.fold(Response.text, Response.text))
    yield response


object UpdateNoteHandlerLive:
  
  lazy val layer: URLayer[UpdateNoteService, UpdateNoteHandler] = ZLayer.fromFunction(UpdateNoteHandlerLive.apply)
