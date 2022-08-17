package route.handler

import model.Note
import route.interface.CanCreateRecord
import route.implementation.CreateNoteService
import zhttp.http.Response
import zio.*
import zhttp.http.Request
import zio.json.*

import java.net.http.HttpResponse.ResponseInfo

class CreateNoteRoute() {

  private val createNoteRouteService: CanCreateRecord[Note] = new CreateNoteService()

  def handle(request: Request): Task[Response] = for {
    noteAsJson     <- request.bodyAsString
    noteEither     <- ZIO.succeed(noteAsJson.fromJson[Note])
    creationStatus <- noteEither.fold(
      err => ZIO.fail(RuntimeException(err)),
      createNoteRouteService.createRecord
    )
    response       <- ZIO.succeed(creationStatus.fold(
      Response.text,
      Response.text
    ))
  } yield response

}
