package server.endpoint.note

import route.handler.*
import route.implementation.DeleteNoteService
import server.NotesServer
import zhttp.http.*
import zio.*

trait DeleteNoteEndpoint extends HasRoute[Any]

final case class DeleteNoteEndpointLive(deleteNoteHandler: DeleteNoteHandler) extends DeleteNoteEndpoint {

  override lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case Method.DELETE -> !! / "api" / "notes" / int(noteId) => deleteNoteHandler handle noteId
  }

}

object DeleteNoteEndpointLive {
  
  lazy val layer: URLayer[DeleteNoteHandler, DeleteNoteEndpoint] =
    ZLayer.fromFunction(DeleteNoteEndpointLive.apply _)
  
}