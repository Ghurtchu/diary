package route.implementation

import db.CRUD.DeletionStatus
import db.{CRUD, NotesRepository}
import model.Note
import route.interface.RecordRemover
import zhttp.http.Response
import zio.*

class DeleteNoteService(notesRepository: CRUD[Note]) extends RecordRemover {

  override def deleteRecord(id: Int): Task[Either[String, String]] = 
    notesRepository delete id

}

object DeleteNoteService {
  def spawn(notesRepository: CRUD[Note]): DeleteNoteService = new DeleteNoteService(notesRepository)
  
  def layer: ULayer[DeleteNoteService] = NotesRepository.layer >>> ZLayer.fromFunction(DeleteNoteService.spawn)
}
