package route.handler

import model.Note
import route.interface
import interface._
import route.implementation.GetNoteServiceLive
import zhttp.http.Response
import zio.*
import zio.json.*
import zhttp.http.Status

trait GetNoteHandler:
  def handle(noteId: Long, userId: Long): Task[Response]


final case class GetNoteHandlerLive(getNoteService: GetNoteService) extends GetNoteHandler:

  override def handle(noteId: Long, userId: Long): Task[Response] =
    getNoteService.getNote(noteId, userId)
      .map(_.toResponse)


object GetNoteHandlerLive:

  lazy val layer: URLayer[GetNoteService, GetNoteHandler] = ZLayer.fromFunction(GetNoteHandlerLive.apply)