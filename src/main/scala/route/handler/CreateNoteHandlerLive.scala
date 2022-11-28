package route.handler

import zhttp.http.HttpError.BadRequest
import zhttp.http.{Request, Response, Status}
import zio.*
import zio.json.*
import RequestHandlerDefinitions.CreateNoteHandler
import domain.Domain.{JwtContent, Note}
import route.service.ServiceDefinitions.CreateNoteService

final case class CreateNoteHandlerLive(createNoteService: CreateNoteService) extends CreateNoteHandler:

  override def handle(request: Request, jwtContent: JwtContent): Task[Response] = 
    for 
      noteEither <- request.bodyAsString.map(_.fromJson[Note])
      response   <- noteEither.fold(_ => ZIO.succeed(Response.text("Invalid Json").setStatus(Status.BadRequest)), creationStatusToResponse(jwtContent.userId, _))
    yield response

  private def creationStatusToResponse(userId: Long, note: Note): Task[Response] =
    val noteWithUserId = note.copy(userId = Some(userId))

    createNoteService.createNote(noteWithUserId)
      .map(_.fold(
        err     => Response.text(err).setStatus(Status.BadRequest),
        success => Response.text(success).setStatus(Status.Created))
      )
  

object CreateNoteHandlerLive:

  lazy val layer: URLayer[CreateNoteService, CreateNoteHandler] = 
    ZLayer.fromFunction(CreateNoteHandlerLive.apply)

