package server.endpoint

import model.*
import route.handler.GetNoteHandler
import route.implementation.*
import route.interface.*
import server.NotesServer
import zhttp.http.*
import zio.*

final case class GetNoteEndpoint(getNoteHandler: GetNoteHandler) {

  lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case Method.GET -> !! / "api" / "notes" / noteId => getNoteHandler handle noteId.toInt
  } @@ NotesServer.jwtAuthMiddleware

}

object GetNoteEndpoint {

  lazy val layer: URLayer[GetNoteHandler, GetNoteEndpoint] =
    ZLayer.fromFunction(GetNoteEndpoint.apply _)

}
