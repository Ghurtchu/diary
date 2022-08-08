
import route.NotesRoute

import zio.*
import zhttp.*
import zhttp.http._
import zhttp.service.Server

object NotesServer extends ZIOAppDefault {

  val app = Http.collectZIO[Request] {
    case Method.GET -> !! / "notes"      => NotesRoute.notes()
    case Method.GET -> !! / "notes" / id => NotesRoute note id.toInt
  }

  override def run = Server.start(8080, app)
    .provideLayer(ZLayer.succeed(ZIO.succeed(())))
}
