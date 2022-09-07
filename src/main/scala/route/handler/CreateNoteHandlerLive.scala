package route.handler

import model.{JwtContent, Note}
import route.interface.*
import zhttp.http.HttpError.BadRequest
import zhttp.http.{Request, Response, Status}
import zio.*
import zio.json.*

import java.net.http.HttpResponse.ResponseInfo

trait CreateNoteHandler:
  
  def handle(request: Request, jwtContent: JwtContent): Task[Response]


final case class CreateNoteHandlerLive(createNoteService: CreateNoteService) extends CreateNoteHandler:

  override def handle(request: Request, jwtContent: JwtContent): Task[Response] = 
    for 
      noteEither <- request.bodyAsString.map(_.fromJson[Note])
      response   <- noteEither.fold(
        _ => ZIO.succeed(Response.text("Invalid Json").setStatus(Status.BadRequest)), 
        parseNoteCreationStatusToResponse(jwtContent.userId, _))
    yield response

  private def parseNoteCreationStatusToResponse(userId: Long, note: Note) =
    val noteWithUserId = note.copy(userId = Some(userId))

    createNoteService.createNote(noteWithUserId)
      .map(_.fold(Response.text, Response.text))
  

object CreateNoteHandlerLive:

  lazy val layer: URLayer[CreateNoteService, CreateNoteHandler] = 
    ZLayer.fromFunction(CreateNoteHandlerLive.apply)

