package route.implementation

import db.*
import db.note.NotesRepository
import model.Note
import route.interface.GetNoteService
import zhttp.http.Response
import zio.*
import zio.json.*

final case class GetNoteServiceLive(private final val notesRepository: NotesRepository) extends GetNoteService:

  override def getNote(noteId: Long, userId: Long): Task[Either[String, Note]] =
    notesRepository
      .getNoteByIdAndUserId(noteId, userId)
      .map(_.fold(Left(s"Could not find the note with id ${noteId.inQuotes}"))(Right(_)))

object GetNoteServiceLive:
  
  lazy val layer: URLayer[NotesRepository, GetNoteService] = 
    ZLayer.fromFunction(GetNoteServiceLive.apply)