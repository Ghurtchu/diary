package route.handler

import zhttp.http.Response
import zio.*
import zio.json.*
import zhttp.http.Status
import RequestHandlerDefinitions.GetNoteHandler
import route.service.GetNoteServiceLive
import route.service.ServiceDefinitions.GetNoteService

final case class GetNoteHandlerLive(getNoteService: GetNoteService) extends GetNoteHandler:

  override def handle(noteId: Long, userId: Long): Task[Response] =
    getNoteService.getNote(noteId, userId)
      .map(_.notFoundOrFound)


object GetNoteHandlerLive:

  lazy val layer: URLayer[GetNoteService, GetNoteHandler] = ZLayer.fromFunction(GetNoteHandlerLive.apply)