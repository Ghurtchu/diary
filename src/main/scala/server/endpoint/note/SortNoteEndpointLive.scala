package server.endpoint.note

import domain.*
import route.handler.*
import route.handler.RequestHandlerDefinitions.SortNoteHandler
import server.NotesServer
import server.endpoint.note.NoteEndpointDefinitions.SortNoteEndpoint
import server.middleware.{RequestContextManager, RequestContextMiddleware}
import zhttp.http.*
import zio.*

import scala.sort.SortNoteService

final case class SortNoteEndpointLive(sortNoteHandler: SortNoteHandler) extends SortNoteEndpoint:

  override lazy val route: HttpApp[RequestContextManager, Throwable] = Http.collectZIO[Request] {
    case request@Method.GET -> !! / "api" / "notes" / "sort" =>
      for
        requestContext <- ZIO.service[RequestContextManager].flatMap(_.getCtx)
        response       <- requestContext.getJwtOrFail.fold(identity, sortNoteHandler.handle(request, _))
      yield response
  } @@ RequestContextMiddleware.jwtAuthMiddleware


object SortNoteEndpointLive:
  
  lazy val layer: URLayer[SortNoteHandler, SortNoteEndpoint] =
    ZLayer.fromFunction(SortNoteEndpointLive.apply)