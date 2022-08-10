
import route.NotesRoute

import zio.*
import zhttp.*
import zhttp.http._
import zhttp.service.Server

object NotesServer extends ZIOAppDefault {

  val app: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {
    case Method.GET         -> !! / "api" / "notes"            => NotesRoute.getAllNotes
    case req @ Method.POST  -> !! / "api" / "notes"            => req.bodyAsString.flatMap(NotesRoute.addNote)
    case req @ Method.POST  -> !! / "api" / "notes" / "search" => NotesRoute.searchByTitle(req.url.queryParams("title").head)
    case Method.POST        -> !! / "api" / "notes" / id       => NotesRoute getNoteById id.toInt
    case Method.DELETE      -> !! / "api" / "notes" / id       => NotesRoute removeNoteById id.toInt
    case req @ Method.PUT   -> !! / "api" / "notes" / id       => req.bodyAsString.flatMap(noteAsString => NotesRoute.updateNote(id.toInt, noteAsString))
  }

  override def run = Server.start(8080, app)
    .provideLayer(ZLayer.succeed(ZIO.succeed(())))
}
