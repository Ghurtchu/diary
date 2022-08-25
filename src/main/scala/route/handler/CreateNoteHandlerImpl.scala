package route.handler

import model.Note
import route.interface._
import zhttp.http.Response
import zio.*
import zhttp.http.Request
import zio.json.*

import java.net.http.HttpResponse.ResponseInfo

trait CreateNoteHandler {
  def handle(request: Request): Task[Response]
}

final case class CreateNoteHandlerImpl(createNoteService: RecordCreator[Note]) extends CreateNoteHandler {

  final override def handle(request: Request): Task[Response] = for {
    noteAsJson        <- request.bodyAsString
    noteEither        <- ZIO.succeed(noteAsJson.fromJson[Note])
    creationStatus    <- noteEither.fold(
      err => ZIO.fail(RuntimeException(err)),
      createNoteService.createRecord
    )
    response          <- ZIO.succeed(creationStatus.fold(
      Response.text,
      Response.text
    ))
  } yield response

}

object CreateNoteHandlerImpl {

  lazy val layer: URLayer[RecordCreator[Note], CreateNoteHandlerImpl] = ZLayer.fromFunction(CreateNoteHandlerImpl.apply _)

}
