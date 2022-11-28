package route.handler

import db.note.NotesRepositoryLive
import route.implementation
import route.interface._
import route.implementation.DeleteNoteServiceLive
import zhttp.http.Response
import zio.*
import zhttp.http._
import RequestHandlerDefinitions.DeleteNoteHandler

final case class DeleteNoteHandlerLive(deleteNoteService: DeleteNoteService) extends DeleteNoteHandler:

  override def handle(noteId: Long, userId: Long): Task[Response] = 
    deleteNoteService.deleteRecord(noteId, userId)
      .map(_.fold(
        err     => Response.text(err).setStatus(Status.BadRequest),
        success => Response.text(success).setStatus(Status.Ok)
      ))
    

object DeleteNoteHandlerLive:
  
  lazy val layer: URLayer[DeleteNoteService, DeleteNoteHandler] = ZLayer.fromFunction(DeleteNoteHandlerLive.apply)
