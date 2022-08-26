package server.endpoint

import route.handler.GetAllNotesHandler
import server._
import zhttp.http.*
import zio.*

final case class GetAllNotesEndpoint(getAllNotesHandler: GetAllNotesHandler) {

  lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case Method.GET -> !! / "api" / "notes" => getAllNotesHandler.handle
  } @@ NotesServer.jwtAuthMiddleware

}

object GetAllNotesEndpoint {
  
  def layer: URLayer[GetAllNotesHandler, GetAllNotesEndpoint] =
    ZLayer.fromFunction(GetAllNotesEndpoint.apply _)
  
}
