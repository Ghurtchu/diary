package server.endpoint

import route.handler.*
import zhttp.http.*
import route.interface.*
import model.*
import zio._
import route.implementation.UpdateNoteService

final case class UpdateNoteEndpoint(updateNoteHandler: UpdateNoteHandler) {

  lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case request@Method.PUT -> !! / "api" / "notes" / id => updateNoteHandler.handle(request, id.toInt)
  }

}

object UpdateNoteEndpoint {

  lazy val layer: URLayer[UpdateNoteHandler, UpdateNoteEndpoint] =
    ZLayer.fromFunction(UpdateNoteEndpoint.apply _)

}
