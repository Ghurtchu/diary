package server.endpoint

import route.handler.*
import zhttp.http._
import zio._

final case class SearchNoteEndpoint(searchNoteHandler: SearchNoteHandler) {
  
  lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case request@Method.GET -> !! / "api" / "notes" / "sort" => searchNoteHandler handle request 
  }
  
}

object SearchNoteEndpoint {
  
  lazy val layer: URLayer[SearchNoteHandler, SearchNoteEndpoint] =
    ZLayer.fromFunction(SearchNoteEndpoint.apply _)
  
}
