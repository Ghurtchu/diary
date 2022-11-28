package server.endpoint.note

import domain.*
import route.handler.*
import route.handler.RequestHandlerDefinitions.UpdateNoteHandler
import route.service.UpdateNoteServiceLive
import server.NotesServer
import server.endpoint.note.NoteEndpointDefinitions.UpdateNoteEndpoint
import zhttp.http.*
import zio.*

final case class UpdateNoteEndpointLive(updateNoteHandler: UpdateNoteHandler) extends UpdateNoteEndpoint:

  override lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case request@Method.PUT -> !! / "api" / "notes" / long(noteId) => updateNoteHandler.handle(request, noteId)
  } 


object UpdateNoteEndpointLive:

  lazy val layer: URLayer[UpdateNoteHandler, UpdateNoteEndpoint] =
    ZLayer.fromFunction(UpdateNoteEndpointLive.apply)

