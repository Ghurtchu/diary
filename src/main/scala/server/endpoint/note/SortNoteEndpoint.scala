package server.endpoint.note

import model.*
import route.handler.*
import route.interface.*
import server.NotesServer
import util.sort.SortNoteService
import zhttp.http.*
import zio.*

trait SortNoteEndpoint extends HasRoute[Any]

final case class SortNoteEndpointLive(sortNoteHandler: SortNoteHandler) extends SortNoteEndpoint {

  override lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case request@Method.GET -> !! / "api" / "notes" / "sort" => sortNoteHandler handle request
  } 

}

object SortNoteEndpointLive {
  
  lazy val layer: URLayer[SortNoteHandler, SortNoteEndpoint] =
    ZLayer.fromFunction(SortNoteEndpointLive.apply _)
  
}