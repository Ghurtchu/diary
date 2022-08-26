package server.endpoint

import route.handler.*
import server.NotesServer
import zhttp.http.*
import zio.*

final case class SearchNoteEndpoint(searchNoteHandler: SearchNoteHandler) {
  
  lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case request@Method.GET -> !! / "api" / "notes" / "sort" => searchNoteHandler handle request 
  } @@ NotesServer.jwtAuthMiddleware
  
}

object SearchNoteEndpoint {
  
  lazy val layer: URLayer[SearchNoteHandler, SearchNoteEndpoint] =
    ZLayer.fromFunction(SearchNoteEndpoint.apply _)
  
}
