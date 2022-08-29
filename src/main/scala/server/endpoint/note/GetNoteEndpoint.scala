package server.endpoint.note

import model.*
import route.handler.GetNoteHandler
import route.implementation.*
import route.interface.*
import server.NotesServer
import server.middleware.{RequestContextManager, RequestContextMiddleware}
import zhttp.http.*
import zio.*

trait GetNoteEndpoint extends HasRoute[RequestContextManager]

final case class GetNoteEndpointLive(getNoteHandler: GetNoteHandler) extends GetNoteEndpoint {

  override lazy val route: HttpApp[RequestContextManager, Throwable] = Http.collectZIO[Request] {
    case Method.GET -> !! / "api" / "notes" / int(noteId) => getNoteHandler handle noteId
  } @@ RequestContextMiddleware.jwtAuthMiddleware

}

object GetNoteEndpointLive {

  lazy val layer: URLayer[GetNoteHandler, GetNoteEndpoint] =
    ZLayer.fromFunction(GetNoteEndpointLive.apply _)

}
