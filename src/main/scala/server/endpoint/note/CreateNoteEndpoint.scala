package server.endpoint.note

import route.handler.{CreateNoteHandler, SignupHandler}
import route.implementation.CreateNoteServiceLive
import server.NotesServer
import server.middleware.{RequestContextManager, RequestContextMiddleware}
import zhttp.*
import zhttp.http.*
import zio.*

trait CreateNoteEndpoint extends HasRoute[RequestContextManager]

final case class CreateNoteEndpointLive(createNoteHandler: CreateNoteHandler) extends CreateNoteEndpoint:

  override lazy val route: HttpApp[RequestContextManager, Throwable] = Http.collectZIO[Request] {
    case request @ Method.POST -> !! / "api" / "notes" => 
      for
        requestContext <- ZIO.service[RequestContextManager].flatMap(_.getCtx) 
        response       <- requestContext.getJwtOrFailure.fold(identity, createNoteHandler.handle(request, _))
      yield response
  } @@ RequestContextMiddleware.jwtAuthMiddleware

object CreateNoteEndpointLive:

  lazy val layer: URLayer[CreateNoteHandler, CreateNoteEndpoint] =
    ZLayer.fromFunction(CreateNoteEndpointLive.apply)

