package server.endpoint.note

import route.handler.{CreateNoteHandler, SignupHandler}
import route.implementation.CreateNoteService
import server.NotesServer
import server.middleware.{RequestContextManager, RequestContextMiddleware}
import zhttp.*
import zhttp.http.*
import zio.*

trait CreateNoteEndpoint extends HasRoute[RequestContextManager]

final case class CreateNoteEndpointLive(createNoteHandler: CreateNoteHandler) extends CreateNoteEndpoint {

  override lazy val route: HttpApp[RequestContextManager, Throwable] = Http.collectZIO[Request] {
    case request@Method.POST -> !! / "api" / "notes" => for {
      reqCtxManager <- ZIO.service[RequestContextManager]
      ctx <- reqCtxManager.getCtx
      jwtContent <- ZIO.succeed(ctx.jwtContent.get)
      response <- createNoteHandler.handle(request, jwtContent)
    } yield response
  } @@ RequestContextMiddleware.jwtAuthMiddleware
}

object CreateNoteEndpointLive {

  lazy val layer: URLayer[CreateNoteHandler, CreateNoteEndpoint] =
    ZLayer.fromFunction(CreateNoteEndpointLive.apply _)

}
