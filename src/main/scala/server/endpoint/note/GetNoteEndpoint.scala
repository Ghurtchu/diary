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


final case class GetNoteEndpointLive(getNoteHandler: GetNoteHandler) extends GetNoteEndpoint:

  override lazy val route: HttpApp[RequestContextManager, Throwable] = Http.collectZIO[Request] {
    case Method.GET -> !! / "api" / "notes" / long(noteId) =>
      for
        requestContext <- ZIO.service[RequestContextManager].flatMap(_.getCtx)
        response       <- requestContext.getJwtOrFailure.fold(identity, jwtContent => getNoteHandler.handle(noteId, jwtContent.userId)) 
      yield response
  } @@ RequestContextMiddleware.jwtAuthMiddleware


object GetNoteEndpointLive:

  lazy val layer: URLayer[GetNoteHandler, GetNoteEndpoint] =
    ZLayer.fromFunction(GetNoteEndpointLive.apply)
