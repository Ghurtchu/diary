package server.endpoint.note

import model.*
import route.handler.GetNoteHandler
import route.implementation.*
import route.interface.*
import server.NotesServer
import zhttp.http.*
import zio.*

trait GetNoteEndpoint extends HasRoute[Any]

final case class GetNoteEndpointLive(getNoteHandler: GetNoteHandler) extends GetNoteEndpoint {

  override lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case Method.GET -> !! / "api" / "notes" / int(noteId) => getNoteHandler handle noteId
  }

}

object GetNoteEndpointLive {

  lazy val layer: URLayer[GetNoteHandler, GetNoteEndpoint] =
    ZLayer.fromFunction(GetNoteEndpointLive.apply _)

}
