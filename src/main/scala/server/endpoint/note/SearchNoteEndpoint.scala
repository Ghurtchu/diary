package server.endpoint.note

import route.handler.*
import server.NotesServer
import zhttp.http.*
import zio.*

trait SearchNoteEndpoint extends HasRoute[Any]

final case class SearchNoteEndpointLive(searchNoteHandler: SearchNoteHandler) extends SearchNoteEndpoint {
  
  override lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case request@Method.GET -> !! / "api" / "notes" / "sort" => searchNoteHandler handle request 
  } @@ NotesServer.jwtAuthMiddleware
  
}

object SearchNoteEndpointLive {
  
  lazy val layer: URLayer[SearchNoteHandler, SearchNoteEndpoint] =
    ZLayer.fromFunction(SearchNoteEndpointLive.apply _)
  
}
