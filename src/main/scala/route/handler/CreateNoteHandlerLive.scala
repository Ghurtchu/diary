package route.handler

import model.{JwtContent, Note}
import route.interface.*
import zhttp.http.Response
import zio.*
import zhttp.http.Request
import zio.json.*

import java.net.http.HttpResponse.ResponseInfo

trait CreateNoteHandler {
  def handle(request: Request, jwtContent: JwtContent): Task[Response]
}

final case class CreateNoteHandlerLive(createNoteService: RecordCreator[Note]) extends CreateNoteHandler {

  override def handle(request: Request, jwtContent: JwtContent): Task[Response] = for {
    noteAsJson        <- request.bodyAsString
    noteEither        <- ZIO.succeed(noteAsJson.fromJson[Note])
    creationStatus    <- noteEither.fold(
      err => ZIO.fail(RuntimeException(err)),
      note => {
        val noteWithUserId = note.copy(userId = Some(jwtContent.id))
        createNoteService createRecord noteWithUserId
      }
    )
    response          <- ZIO.succeed(creationStatus.fold(
      Response.text,
      Response.text
    ))
  } yield response

}

object CreateNoteHandlerLive {

  lazy val layer: URLayer[RecordCreator[Note], CreateNoteHandler] = ZLayer.fromFunction(CreateNoteHandlerLive.apply _)

}
