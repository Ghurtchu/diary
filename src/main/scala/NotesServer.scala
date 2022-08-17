
import db.InMemoryDB
import model.Note
import model.AuthPayload
import route.implementation.*
import route.interface.*
import zio.*
import zhttp.*
import zhttp.http.*
import service.Server
import zhttp.http.HttpError.BadRequest
import zio.json.*

object NotesServer extends ZIOAppDefault {

  val httpApp: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {

    case request@Method.POST -> !! / "api" / "user" / "signup" => SignupRoute().handle(request)

    case Method.GET      -> !! / "api" / "notes" => GetAllNotesRoute().handle

    case request@Method.POST -> !! / "api" / "notes" => CreateNoteRoute().handle(request)

    case request@Method.GET -> !! / "api" / "notes" / "search" => SearchNoteRoute().handle(request)

    case req@Method.GET  -> !! / "api" / "notes" / "sort" => for {
      order <- ZIO.succeed {
        val queryParamsMap = req.url.queryParams

        queryParamsMap.get("order")
          .fold("asc")(_.head)
      }
      notes <- ZIO.succeed(InMemoryDB.notes)
      ordered <- ZIO.succeed {
        val sortLogic: (Note, Note) => Boolean =
          if order == "desc" then _.title > _.title else _.title < _.title

        notes sortWith sortLogic
      }
      response <- ZIO.succeed(Response.text(ordered.toJsonPretty))
    } yield response

    case Method.GET -> !! / "api" / "notes" / id => GetNoteRoute().handle(id.toInt)

    case Method.DELETE -> !! / "api" / "notes" / id => DeleteNoteRoute().handle(id.toInt)

    case request@Method.PUT -> !! / "api" / "notes" / id => UpdateNoteRoute().handle(request, id.toInt)

  }
  override def run = Server.start(5555, httpApp)
    .provideLayer(ZLayer.succeed(ZIO.succeed(())))
}
