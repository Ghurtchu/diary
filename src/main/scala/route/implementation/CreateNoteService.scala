package route.implementation

import db.{CRUD, NotesRepository}
import model.Note
import route.interface.RecordCreator
import zhttp.http.Response
import zio.*
import zio.json.*

class CreateNoteService(private final val notesRepository: CRUD[Note]) extends RecordCreator[Note] {

  override def createRecord(note: Note): Task[Either[String, String]] = 
    notesRepository.add(note)

}

object CreateNoteService {
  
  def spawn(notesRepository: CRUD[Note]): CreateNoteService = 
    new CreateNoteService(notesRepository)
    
  def layer: ULayer[CreateNoteService] = 
    NotesRepository.layer >>> ZLayer.fromFunction(CreateNoteService.spawn)

}
