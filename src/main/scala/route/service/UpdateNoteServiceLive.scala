package route.service

import db.note.{NotesRepository, NotesRepositoryLive}
import ServiceDefinitions.UpdateNoteService
import domain.Domain.Note
import zhttp.http.Response
import zio.*
import zio.json.*

final case class UpdateNoteServiceLive(private val notesRepository: NotesRepository) extends UpdateNoteService:
  
  override def updateNote(noteId: Long, note: Note): Task[Either[String, String]] = 
    notesRepository.update(noteId, note.copy(id = Some(noteId)))
      .map(_.toDBResultMessage)

object UpdateNoteServiceLive:
  
  lazy val layer: URLayer[NotesRepository, UpdateNoteService] = ZLayer.fromFunction(UpdateNoteServiceLive.apply)

