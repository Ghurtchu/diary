package server.endpoint

import route.handler.*
import zhttp.http.*
import route.handler.*
import route.implementation.DeleteNoteService
import server.NotesServer
import zio.*

final case class DeleteNoteEndpoint(deleteNoteHandler: DeleteNoteHandler) {

  lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case Method.DELETE -> !! / "api" / "notes" / noteId => deleteNoteHandler handle noteId.toInt
  } @@ NotesServer.jwtAuthMiddleware

}

object DeleteNoteEndpoint {
  
  lazy val layer: URLayer[DeleteNoteHandler, DeleteNoteEndpoint] =
    ZLayer.fromFunction(DeleteNoteEndpoint.apply _)
  
}