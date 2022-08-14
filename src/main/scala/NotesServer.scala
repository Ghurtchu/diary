
import route.implementation._
import route.interface._
import zio.*
import zhttp.*
import zhttp.http.*
import service.Server
import zio.json._


object NotesServer extends ZIOAppDefault {

  val httpApp: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {
    case Method.GET        -> !! / "api" / "notes"            => GetAllNotesRoute().handle
    case req @ Method.POST -> !! / "api" / "notes"            => req.bodyAsString.flatMap(CreateNoteRoute(_).handle)
    case req @ Method.POST -> !! / "api" / "notes" / "search" => SearchNoteRoute().handle(req.url.queryParams("title").head)
    case Method.GET        -> !! / "api" / "notes" / id       => GetNoteRoute().handle(id.toInt)
    case Method.DELETE     -> !! / "api" / "notes" / id       => DeleteNoteRoute().handle(id.toInt)
    case req @ Method.PUT  -> !! / "api" / "notes" / id       => req.bodyAsString.flatMap(noteAsString => UpdateNoteRoute().handle(id.toInt, noteAsString))
  }

  override def run = Server.start(5555, httpApp)
    .provideLayer(ZLayer.succeed(ZIO.succeed(())))
}
