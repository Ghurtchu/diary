package route.handler

import route.implementation
import route.interface._
import route.implementation.DeleteNoteServiceLive
import zhttp.http.Response
import zio.*
import zhttp.http._
import DeleteNoteHandlerLive.NoteID
import db.NotesRepositoryLive


trait DeleteNoteHandler {
  def handle(noteId: NoteID, userId: Long): Task[Response]
}

final case class DeleteNoteHandlerLive(deleteNoteService: DeleteNoteService) extends DeleteNoteHandler {

  override def handle(noteId: NoteID, userId: Long): Task[Response] = for {
    deleteStatus <- deleteNoteService.deleteRecord(noteId, userId)
    response     <- ZIO.succeed(deleteStatus.fold(Response.text, Response.text))
  } yield response

}

object DeleteNoteHandlerLive {
  
  type NoteID = Long
 
  lazy val layer: URLayer[DeleteNoteService, DeleteNoteHandler] = ZLayer.fromFunction(DeleteNoteHandlerLive.apply)
  
}