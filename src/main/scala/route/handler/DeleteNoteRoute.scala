package route.handler

import db.NotesRepository
import route.implementation
import route.interface._
import route.implementation.DeleteNoteService
import zhttp.http.Response
import zio.*
import DeleteNoteRoute.NoteID

object DeleteNoteRoute {
  type NoteID = Int
}

class DeleteNoteRoute extends CommonRequestHandler[NoteID, RecordRemover] {

  final override def handle(noteId: NoteID): RIO[RecordRemover, Response] = for {
    deleteNoteService <- ZIO.service[RecordRemover]
    deleteStatus      <- deleteNoteService.deleteRecord(noteId)
    response          <- ZIO.succeed(deleteStatus.fold(Response.text, Response.text))
  } yield response

}
