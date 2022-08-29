package server.endpoint.note

import model.*
import route.handler.*
import route.interface.*
import server.NotesServer
import server.middleware.{RequestContextManager, RequestContextMiddleware}
import util.sort.SortNoteService
import zhttp.http.*
import zio.*

trait SortNoteEndpoint extends HasRoute[RequestContextManager]

final case class SortNoteEndpointLive(sortNoteHandler: SortNoteHandler) extends SortNoteEndpoint {

  override lazy val route: HttpApp[RequestContextManager, Throwable] = Http.collectZIO[Request] {
    case request@Method.GET -> !! / "api" / "notes" / "sort" => for {
      jwtContent <- ZIO.service[RequestContextManager].flatMap(_.getCtx.map(_.jwtContent))
      response    <- sortNoteHandler.handle(request, jwtContent.get)
    } yield response
  } @@ RequestContextMiddleware.jwtAuthMiddleware

}

object SortNoteEndpointLive {
  
  lazy val layer: URLayer[SortNoteHandler, SortNoteEndpoint] =
    ZLayer.fromFunction(SortNoteEndpointLive.apply _)
  
}