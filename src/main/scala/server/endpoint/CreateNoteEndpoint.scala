package server.endpoint

import route.handler.{CreateNoteHandler, SignupHandler}
import route.implementation.CreateNoteService
import zhttp.*
import zhttp.http.*
import zio.*

final case class CreateNoteEndpoint(createNoteHandler: CreateNoteHandler) {

  lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case request@Method.POST -> !! / "api" / "notes" => createNoteHandler handle request
  }

}

object CreateNoteEndpoint {

  lazy val layer: URLayer[CreateNoteHandler, CreateNoteEndpoint] =
    ZLayer.fromFunction(CreateNoteEndpoint.apply _)

}
