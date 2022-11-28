package server.endpoint.note

import route.handler.RequestHandlerDefinitions.GetAllNotesHandler
import server.*
import server.endpoint.note.NoteEndpointDefinitions.GetAllNotesEndpoint
import server.middleware.{RequestContext, RequestContextManager, RequestContextMiddleware}
import zhttp.http.*
import zio.*

final case class GetAllNotesEndpointLive(getAllNotesHandler: GetAllNotesHandler) extends GetAllNotesEndpoint:

  override lazy val route: HttpApp[RequestContextManager, Throwable] = Http.collectZIO[Request] {
    case Method.GET -> !! / "api" / "notes" =>
      for
        requestContext <- ZIO.service[RequestContextManager].flatMap(_.getCtx)
        response       <- requestContext.getJwtOrFail.fold(identity, getAllNotesHandler.handle)
      yield response
  } @@ RequestContextMiddleware.jwtAuthMiddleware


object GetAllNotesEndpointLive:
  
  def layer: URLayer[GetAllNotesHandler, GetAllNotesEndpoint] =
    ZLayer.fromFunction(GetAllNotesEndpointLive.apply)
