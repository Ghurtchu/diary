package route.handler

import db.NotesRepository
import route.implementation
import route.interface._
import route.implementation.DeleteNoteService
import zhttp.http.Response
import zio.*
import zhttp.http._
import DeleteNoteHandlerImpl.NoteID


trait DeleteNoteHandler {
  def handle(noteId: NoteID): Task[Response]
}

final case class DeleteNoteHandlerImpl(deleteNoteService: RecordRemover) extends DeleteNoteHandler {

  final override def handle(noteId: NoteID): Task[Response] = for {
    deleteStatus      <- deleteNoteService.deleteRecord(noteId)
    response          <- ZIO.succeed(deleteStatus.fold(Response.text, Response.text))
  } yield response

}

object DeleteNoteHandlerImpl {
  
  type NoteID = Int
 
  lazy val layer: URLayer[RecordRemover, DeleteNoteHandlerImpl] = ZLayer.fromFunction(DeleteNoteHandlerImpl.apply _)
  
}