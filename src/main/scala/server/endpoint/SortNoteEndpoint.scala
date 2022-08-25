package server.endpoint

import zio._
import route.interface.*
import model.*
import route.handler._
import util.sort.SortNoteService
import zhttp.http._

final case class SortNoteEndpoint(sortNoteHandler: SortNoteHandler) {

  lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case request@Method.GET -> !! / "api" / "notes" / "sort" => sortNoteHandler handle request
  }

}

object SortNoteEndpoint {
  
  lazy val layer: URLayer[SortNoteHandler, SortNoteEndpoint] =
    ZLayer.fromFunction(SortNoteEndpoint.apply _)
  
}