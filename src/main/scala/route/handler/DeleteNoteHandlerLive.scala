package route.handler

import db.NotesRepository
import route.implementation
import route.interface._
import route.implementation.DeleteNoteServiceLive
import zhttp.http.Response
import zio.*
import zhttp.http._
import DeleteNoteHandlerLive.NoteID


trait DeleteNoteHandler {
  def handle(noteId: NoteID, userId: Int): Task[Response]
}

final case class DeleteNoteHandlerLive(deleteNoteService: DeleteNoteService) extends DeleteNoteHandler {

  override def handle(noteId: NoteID, userId: Int): Task[Response] = for {
    deleteStatus      <- deleteNoteService.deleteRecord(noteId, userId)
    response          <- ZIO.succeed(deleteStatus.fold(Response.text, Response.text))
  } yield response

}

object DeleteNoteHandlerLive {
  
  type NoteID = Int
 
  lazy val layer: URLayer[DeleteNoteService, DeleteNoteHandler] = ZLayer.fromFunction(DeleteNoteHandlerLive.apply _)
  
}