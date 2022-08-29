package server.endpoint.note

import route.handler.GetAllNotesHandler
import server.*
import server.middleware.{RequestContext, RequestContextManager, RequestContextMiddleware}
import zhttp.http.*
import zio.*

trait GetAllNotesEndpoint extends HasRoute[RequestContextManager]

final case class GetAllNotesEndpointLive(getAllNotesHandler: GetAllNotesHandler) extends GetAllNotesEndpoint {

  override lazy val route: HttpApp[RequestContextManager, Throwable] = Http.collectZIO[Request] {
    case Method.GET -> !! / "api" / "notes" =>
      for {
        requestContext <- ZIO.service[RequestContextManager].flatMap(_.getCtx)
        response       <- requestContext.getJwtOrFailure.fold(identity, getAllNotesHandler.handle)
    } yield response
  } @@ RequestContextMiddleware.jwtAuthMiddleware

}

object GetAllNotesEndpointLive {
  
  def layer: URLayer[GetAllNotesHandler, GetAllNotesEndpoint] =
    ZLayer.fromFunction(GetAllNotesEndpointLive.apply _)

}