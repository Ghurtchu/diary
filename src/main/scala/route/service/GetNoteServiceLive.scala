package route.service

import db.*
import db.note.NotesRepository
import zhttp.http.Response
import zio.*
import zio.json.*
import ServiceDefinitions.GetNoteService
import domain.Domain.Note

final case class GetNoteServiceLive(private val notesRepository: NotesRepository) extends GetNoteService:

  override def getNote(noteId: Long, userId: Long): Task[Either[String, Note]] =
    notesRepository
      .getNoteByIdAndUserId(noteId, userId)
      .map(_.fold(Left(s"Could not find the note with id ${noteId.inQuotes}"))(Right(_)))

object GetNoteServiceLive:
  
  lazy val layer: URLayer[NotesRepository, GetNoteService] = 
    ZLayer.fromFunction(GetNoteServiceLive.apply)