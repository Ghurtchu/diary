
import route.NotesRoute

import zio.*
import zhttp.*
import zhttp.http._
import zhttp.service.Server

object NotesServer extends ZIOAppDefault {

  val app = Http.collectZIO[Request] {
    case Method.GET  -> !! / "api" / "notes"                    => NotesRoute.getAllNotes
    case Method.POST -> !! / "api" / "notes" / id               => NotesRoute getNoteById id.toInt
    case Method.POST -> !! / "api" / "notes" / "search" / title => NotesRoute searchByTitle title
  }

  override def run = Server.start(8080, app)
    .provideLayer(ZLayer.succeed(ZIO.succeed(())))
}
