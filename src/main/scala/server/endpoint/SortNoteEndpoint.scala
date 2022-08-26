package server.endpoint

import zio.*
import route.interface.*
import model.*
import route.handler.*
import server.NotesServer
import util.sort.SortNoteService
import zhttp.http.*

final case class SortNoteEndpoint(sortNoteHandler: SortNoteHandler) {

  lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case request@Method.GET -> !! / "api" / "notes" / "sort" => sortNoteHandler handle request
  } @@ NotesServer.jwtAuthMiddleware

}

object SortNoteEndpoint {
  
  lazy val layer: URLayer[SortNoteHandler, SortNoteEndpoint] =
    ZLayer.fromFunction(SortNoteEndpoint.apply _)
  
}