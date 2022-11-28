package route.service

import db.Repository.*
import db.note.{NotesRepository, NotesRepositoryLive}
import zhttp.http.Response
import zio.*
import ServiceDefinitions.DeleteNoteService

final case class DeleteNoteServiceLive(private val notesRepository: NotesRepository) extends DeleteNoteService:

  override def deleteRecord(noteId: Long, userId: Long): Task[Either[String, String]] =
    notesRepository.deleteNoteByIdAndUserId(noteId, userId)
      .map(_.toDBResultMessage)

object DeleteNoteServiceLive:

  lazy val layer: URLayer[NotesRepository, DeleteNoteService] =
    ZLayer.fromFunction(DeleteNoteServiceLive.apply)

