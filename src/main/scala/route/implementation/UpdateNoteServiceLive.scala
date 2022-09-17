package route.implementation

import db.note.{NotesRepository, NotesRepositoryLive}
import model.Note
import route.interface.{CreateNoteService, UpdateNoteService}
import zhttp.http.Response
import zio.*
import zio.json.*

final case class UpdateNoteServiceLive(notesRepository: NotesRepository) extends UpdateNoteService:
  
  override def updateNote(noteId: Long, note: Note): Task[Either[String, String]] = 
    notesRepository.update(noteId, note.copy(id = Some(noteId)))
      .map(_.dbOperationMessages)

object UpdateNoteServiceLive:
  
  lazy val layer: URLayer[NotesRepository, UpdateNoteService] = ZLayer.fromFunction(UpdateNoteServiceLive.apply)

