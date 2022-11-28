package route.service

import db.*
import db.note.NotesRepository
import zhttp.http.Response
import zio.*
import zio.json.*
import ServiceDefinitions.GetAllNotesService
import domain.Domain.Note

final case class GetAllNotesServiceLive private(private val notesRepository: NotesRepository) extends GetAllNotesService:

  override def getNotesByUserId(userId: Long): Task[List[Note]] = 
    notesRepository getNotesByUserId userId


object GetAllNotesServiceLive:

  lazy val layer: URLayer[NotesRepository, GetAllNotesService] =
    ZLayer.fromFunction(GetAllNotesServiceLive.apply)
  

