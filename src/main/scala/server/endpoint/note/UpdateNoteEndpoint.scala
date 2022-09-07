package server.endpoint.note

import model.*
import route.handler.*
import route.implementation.UpdateNoteServiceLive
import route.interface.*
import server.NotesServer
import zhttp.http.*
import zio.*

trait UpdateNoteEndpoint extends HasRoute[Any]


final case class UpdateNoteEndpointLive(updateNoteHandler: UpdateNoteHandler) extends UpdateNoteEndpoint:

  override lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case request@Method.PUT -> !! / "api" / "notes" / long(noteId) => updateNoteHandler.handle(request, noteId)
  } 


object UpdateNoteEndpointLive:

  lazy val layer: URLayer[UpdateNoteHandler, UpdateNoteEndpoint] =
    ZLayer.fromFunction(UpdateNoteEndpointLive.apply)

