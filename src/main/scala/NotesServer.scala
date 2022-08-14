
import route.implementation._
import route.interface._
import zio.*
import zhttp.*
import zhttp.http.*
import service.Server
import zio.json.*

object NotesServer extends ZIOAppDefault {

  val httpApp: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {
    case req @ Method.POST -> !! / "api" / "user" / "sign-up" =>  {
      for {
        body      <- req.bodyAsString
        bodyAsMap <- ZIO.succeed(body.fromJson[Map[String, String]])
        statusOrError  <- ZIO.succeed {
          bodyAsMap.map { body =>
            val hasCorrectKeys = body.isDefinedAt("email") && body.isDefinedAt("password")

            hasCorrectKeys
          }
        }
        response <- ZIO.succeed {
          statusOrError.fold(
            err => Response text err,
            status => if status then Response.text("correct data") else Response.text("incorrect data")
          )
        }
      } yield response
    }
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
