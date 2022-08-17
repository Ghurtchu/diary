package route.implementation

import db.CRUD.DeletionStatus
import db.{CRUD, NotesRepository}
import model.Note
import route.interface.CanDeleteRecord
import zhttp.http.Response
import zio.*

class DeleteNoteService extends CanDeleteRecord {

  private val notesRepository: CRUD[Note] = NotesRepository()

  override def deleteRecord(id: Int): Task[Either[String, String]] = 
    notesRepository delete id

}
