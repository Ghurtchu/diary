package route.implementation

import db.{NotesRepository, NotesRepositoryLive}
import model.Note
import route.interface.CreateNoteService
import zhttp.http.Response
import zio.*
import zio.json.*

final case class CreateNoteServiceLive(private final val notesRepository: NotesRepository) extends CreateNoteService:

  override def createNote(note: Note): Task[Either[String, String]] = 
    notesRepository.add(note)
      .map(_.dbOperationMessages)


object CreateNoteServiceLive:
    
  val layer: URLayer[NotesRepository, CreateNoteService] =
    ZLayer.fromFunction(CreateNoteServiceLive.apply)

