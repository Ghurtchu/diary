package route.service

import db.note.{NotesRepository, NotesRepositoryLive}
import zhttp.http.Response
import zio.*
import zio.json.*
import ServiceDefinitions.CreateNoteService
import domain.Domain.Note

final case class CreateNoteServiceLive(private val notesRepository: NotesRepository) extends CreateNoteService:

  override def createNote(note: Note): Task[Either[String, String]] = 
    notesRepository.add(note)
      .map(_.toDBResultMessage)


object CreateNoteServiceLive:
    
  val layer: URLayer[NotesRepository, CreateNoteService] =
    ZLayer.fromFunction(CreateNoteServiceLive.apply)

