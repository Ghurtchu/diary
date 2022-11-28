package route.handler

import db.note.NotesRepositoryLive
import zhttp.http.Response
import zio.*
import zhttp.http.*
import RequestHandlerDefinitions.DeleteNoteHandler
import route.service
import route.service.DeleteNoteServiceLive
import route.service.ServiceDefinitions.DeleteNoteService

final case class DeleteNoteHandlerLive(deleteNoteService: DeleteNoteService) extends DeleteNoteHandler:

  override def handle(noteId: Long, userId: Long): Task[Response] = 
    deleteNoteService.deleteRecord(noteId, userId)
      .map(_.fold(
        err     => Response.text(err).setStatus(Status.BadRequest),
        success => Response.text(success).setStatus(Status.Ok)
      ))
    

object DeleteNoteHandlerLive:
  
  lazy val layer: URLayer[DeleteNoteService, DeleteNoteHandler] = ZLayer.fromFunction(DeleteNoteHandlerLive.apply)
