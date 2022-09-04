package route.handler

import model.Note
import route.interface
import interface._
import route.implementation.GetNoteServiceLive
import zhttp.http.Response
import zio.*
import zio.json.*
import zhttp.http.Status
import GetNoteHandlerLive.NoteID


trait GetNoteHandler {
  def handle(noteId: NoteID, userId: Long): Task[Response]
}

final case class GetNoteHandlerLive(getNoteService: GetNoteService) extends GetNoteHandler {

  override def handle(noteId: NoteID, userId: Long): Task[Response] = for {
    maybeNote      <- getNoteService.getNote(noteId, userId)
    response       <- maybeNote.fold(
      notFound => ZIO.succeed(Response.text(notFound).setStatus(Status.NotFound)),
      note     => ZIO.succeed(Response.text(note.toJson).setStatus(Status.Ok))
    )
  } yield response

}

object GetNoteHandlerLive {
  
  type NoteID = Long
  
  lazy val layer: URLayer[GetNoteService, GetNoteHandler] =
    ZLayer.fromFunction(GetNoteHandlerLive.apply)
  
}