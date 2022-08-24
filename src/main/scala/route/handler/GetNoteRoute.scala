package route.handler

import model.Note
import route.interface
import interface._
import route.implementation.GetNoteService
import zhttp.http.Response
import zio.*
import zio.json.*
import zhttp.http.Status
import GetNoteRoute.NoteID

object GetNoteRoute {
  type NoteID = Int
}

class GetNoteRoute extends CommonRequestHandler[NoteID, RecordRetriever[Note]] { 

  final override def handle(noteId: NoteID): RIO[RecordRetriever[Note], Response] = for {
    getNoteService <- ZIO.service[RecordRetriever[Note]]
    maybeNote      <- getNoteService.retrieveRecord(noteId)
    response       <- maybeNote.fold(
      notFound => ZIO.succeed(Response.text(notFound).setStatus(Status.NotFound)),
      note     => ZIO.succeed(Response.text(note.toJson).setStatus(Status.Ok))
    )
  } yield response

}
