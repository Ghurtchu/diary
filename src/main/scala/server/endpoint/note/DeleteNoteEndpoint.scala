package server.endpoint.note

import route.handler.*
import route.implementation.DeleteNoteServiceLive
import server.NotesServer
import server.middleware.{RequestContext, RequestContextManager, RequestContextMiddleware}
import zhttp.http.*
import zio.*

trait DeleteNoteEndpoint extends HasRoute[RequestContextManager]

final case class DeleteNoteEndpointLive(deleteNoteHandler: DeleteNoteHandler) extends DeleteNoteEndpoint {

  override lazy val route: HttpApp[RequestContextManager, Throwable] = Http.collectZIO[Request] {
    case Method.DELETE -> !! / "api" / "notes" / long(noteId) => for {
        requestContext <- ZIO.service[RequestContextManager].flatMap(_.getCtx)
        response       <- requestContext.getJwtOrFailure.fold(identity, jwtContent => deleteNoteHandler.handle(noteId, jwtContent.id))
      } yield response
  } @@ RequestContextMiddleware.jwtAuthMiddleware

}

object DeleteNoteEndpointLive {
  
  lazy val layer: URLayer[DeleteNoteHandler, DeleteNoteEndpoint] =
    ZLayer.fromFunction(DeleteNoteEndpointLive.apply)
  
}