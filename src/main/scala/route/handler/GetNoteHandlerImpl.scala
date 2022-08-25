package route.handler

import model.Note
import route.interface
import interface._
import route.implementation.GetNoteService
import zhttp.http.Response
import zio.*
import zio.json.*
import zhttp.http.Status
import GetNoteHandlerImpl.NoteID


trait GetNoteHandler {
  def handle(noteId: NoteID): Task[Response]
}

final case class GetNoteHandlerImpl(getNoteService: RecordRetriever[Note]) extends GetNoteHandler { 

  final override def handle(noteId: NoteID): Task[Response] = for {
    maybeNote      <- getNoteService.retrieveRecord(noteId)
    response       <- maybeNote.fold(
      notFound => ZIO.succeed(Response.text(notFound).setStatus(Status.NotFound)),
      note     => ZIO.succeed(Response.text(note.toJson).setStatus(Status.Ok))
    )
  } yield response

}

object GetNoteHandlerImpl {
  
  type NoteID = Int
  
  lazy val layer: URLayer[RecordRetriever[Note], GetNoteHandlerImpl] =
    ZLayer.fromFunction(GetNoteHandlerImpl.apply _)
  
}