package route.implementation

import db.CRUD.DeletionStatus
import db.{CRUD, NoteCRUD, NotesRepository}
import model.Note
import route.interface.DeleteNoteService
import zhttp.http.Response
import zio.*
final case class DeleteNoteServiceLive(notesRepository: NoteCRUD) extends DeleteNoteService {


  override def deleteRecord(noteId: Int, userId: Int): Task[Either[String, String]] = 
    notesRepository.deleteNoteByIdAndUserId(noteId, userId)

}

object DeleteNoteServiceLive {
  
  lazy val layer: URLayer[NoteCRUD, DeleteNoteService] = 
    ZLayer.fromFunction(DeleteNoteServiceLive.apply _)
    
}
