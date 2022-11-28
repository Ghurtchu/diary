package server.endpoint.note.live

import route.handler.RequestHandlerDefinitions.CreateNoteHandler
import route.handler.RequestHandlerDefinitions._
import route.service.CreateNoteServiceLive
import server.NotesServer
import server.endpoint.note.protocol.CreateNoteEndpoint
import server.middleware.{RequestContextManager, RequestContextMiddleware}
import zhttp.*
import zhttp.http.*
import zio.*

final case class CreateNoteEndpointLive(createNoteHandler: CreateNoteHandler) extends CreateNoteEndpoint:

  override lazy val route: HttpApp[RequestContextManager, Throwable] = Http.collectZIO[Request] {
    case request @ Method.POST -> !! / "api" / "notes" => 
      for
        requestContext <- ZIO.service[RequestContextManager].flatMap(_.getCtx) 
        response       <- requestContext.getJwtOrFail.fold(identity, createNoteHandler.handle(request, _))
      yield response
  } @@ RequestContextMiddleware.jwtAuthMiddleware


object CreateNoteEndpointLive:

  lazy val layer: URLayer[CreateNoteHandler, CreateNoteEndpoint] =
    ZLayer.fromFunction(CreateNoteEndpointLive.apply)

