package route.implementation

import db.CRUD.DeletionStatus
import db.note.{NoteCRUD, NotesRepository}
import db.CRUD
import model.Note
import route.interface.DeleteNoteService
import zhttp.http.Response
import zio.*
final case class DeleteNoteServiceLive(notesRepository: NoteCRUD) extends DeleteNoteService {


  override def deleteRecord(noteId: Long, userId: Long): Task[Either[String, String]] =
    notesRepository.deleteNoteByIdAndUserId(noteId, userId)

}

object DeleteNoteServiceLive {
  
  lazy val layer: URLayer[NoteCRUD, DeleteNoteService] = 
    ZLayer.fromFunction(DeleteNoteServiceLive.apply _)
    
}
