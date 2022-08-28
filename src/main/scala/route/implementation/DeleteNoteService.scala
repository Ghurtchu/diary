package route.implementation

import db.CRUD.DeletionStatus
import db.{CRUD, NotesRepository}
import model.Note
import route.interface.RecordRemover
import zhttp.http.Response
import zio.*

final case class DeleteNoteService(notesRepository: CRUD[Note]) extends RecordRemover {

  override def deleteRecord(id: Int): Task[Either[String, String]] = 
    notesRepository delete id

}

object DeleteNoteService {
  
  lazy val layer: URLayer[CRUD[Note], RecordRemover] = 
    ZLayer.fromFunction(DeleteNoteService.apply _)
    
}
