package server.endpoint

import route.handler.*
import zhttp.http.*
import route.interface.*
import model.*
import zio.*
import route.implementation.UpdateNoteService
import server.NotesServer

final case class UpdateNoteEndpoint(updateNoteHandler: UpdateNoteHandler) {

  lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case request@Method.PUT -> !! / "api" / "notes" / id => updateNoteHandler.handle(request, id.toInt)
  } @@ NotesServer.jwtAuthMiddleware

}

object UpdateNoteEndpoint {

  lazy val layer: URLayer[UpdateNoteHandler, UpdateNoteEndpoint] =
    ZLayer.fromFunction(UpdateNoteEndpoint.apply _)

}
