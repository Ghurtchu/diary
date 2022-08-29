package server.endpoint.note

import route.handler.*
import route.implementation.DeleteNoteService
import server.NotesServer
import server.middleware.{RequestContext, RequestContextManager, RequestContextMiddleware}
import zhttp.http.*
import zio.*

trait DeleteNoteEndpoint extends HasRoute[RequestContextManager]

final case class DeleteNoteEndpointLive(deleteNoteHandler: DeleteNoteHandler) extends DeleteNoteEndpoint {

  override lazy val route: HttpApp[RequestContextManager, Throwable] = Http.collectZIO[Request] {
    case Method.DELETE -> !! / "api" / "notes" / int(noteId) => for {
      jwtContent <- ZIO.service[RequestContextManager].flatMap(_.getCtx.map(_.jwtContent.get))
      response   <- deleteNoteHandler.handle(noteId)
    } yield response
  } @@ RequestContextMiddleware.jwtAuthMiddleware

}

object DeleteNoteEndpointLive {
  
  lazy val layer: URLayer[DeleteNoteHandler, DeleteNoteEndpoint] =
    ZLayer.fromFunction(DeleteNoteEndpointLive.apply _)
  
}