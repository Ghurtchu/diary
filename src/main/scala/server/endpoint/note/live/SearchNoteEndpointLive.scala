package server.endpoint.note.live

import route.handler.*
import server.NotesServer
import server.endpoint.note.protocol.SearchNoteEndpoint
import server.middleware.{RequestContextManager, RequestContextMiddleware}
import zhttp.http.*
import zio.*

final case class SearchNoteEndpointLive(searchNoteHandler: SearchNoteHandler) extends SearchNoteEndpoint:
  
  override lazy val route: HttpApp[RequestContextManager, Throwable] = Http.collectZIO[Request] {
    case request@Method.GET -> !! / "api" / "notes" / "search" => 
      for
        requestContext <- ZIO.service[RequestContextManager].flatMap(_.getCtx)
        response       <- requestContext.getJwtOrFail.fold(identity, searchNoteHandler.handle(request, _))
      yield response
  } @@ RequestContextMiddleware.jwtAuthMiddleware


object SearchNoteEndpointLive:
  
  lazy val layer: URLayer[SearchNoteHandler, SearchNoteEndpoint] =
    ZLayer.fromFunction(SearchNoteEndpointLive.apply)
