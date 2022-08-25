package server.endpoint

import route.handler.GetAllNotesHandler
import zhttp.http._
import zio._

final case class GetAllNotesEndpoint(getAllNotesHandler: GetAllNotesHandler) {

  lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case Method.GET -> !! / "api" / "notes" => getAllNotesHandler.handle
  }

}

object GetAllNotesEndpoint {
  
  def layer: URLayer[GetAllNotesHandler, GetAllNotesEndpoint] =
    ZLayer.fromFunction(GetAllNotesEndpoint.apply _)
  
}
