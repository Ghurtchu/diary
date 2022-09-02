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

final case class CreateNoteHandlerLive(createNoteService: CreateNoteService) extends CreateNoteHandler {

  override def handle(request: Request, jwtContent: JwtContent): Task[Response] = for {
    noteEither     <- request.bodyAsString.map(_.fromJson[Note])
    creationStatus <- noteEither.fold(
      parsingError => ZIO.fail(RuntimeException(parsingError)),
      note         => {
        val noteWithUserId = note.copy(userId = Some(jwtContent.id))
        createNoteService createNote noteWithUserId
      }
    )
    response       <- ZIO.succeed(creationStatus.fold(Response.text, Response.text))
  } yield response

}

object CreateNoteHandlerLive {

  lazy val layer: URLayer[CreateNoteService, CreateNoteHandler] = ZLayer.fromFunction(CreateNoteHandlerLive.apply _)

}
