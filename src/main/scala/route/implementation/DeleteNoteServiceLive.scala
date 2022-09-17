package route.implementation

import db.Repository._
import db.note.{NotesRepository, NotesRepositoryLive}
import model.Note
import route.interface.DeleteNoteService
import zhttp.http.Response
import zio.*

final case class DeleteNoteServiceLive(notesRepository: NotesRepository) extends DeleteNoteService:

  override def deleteRecord(noteId: Long, userId: Long): Task[Either[String, String]] =
    notesRepository.deleteNoteByIdAndUserId(noteId, userId)
      .map(_.dbOperationMessages)

object DeleteNoteServiceLive:

  lazy val layer: URLayer[NotesRepository, DeleteNoteService] =
    ZLayer.fromFunction(DeleteNoteServiceLive.apply)

