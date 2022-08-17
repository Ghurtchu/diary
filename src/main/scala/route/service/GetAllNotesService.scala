package route.service

import db.{CRUD, NotesRepository}
import model.Note
import route.interface.CanRetrieveRecords
import zhttp.http.Response
import zio.*
import zio.json.*

class GetAllNotesService extends CanRetrieveRecords[Note] {

  private val notesRepository: CRUD[Note] = NotesRepository()

  override def retrieveRecords: UIO[List[Note]] = 
    notesRepository.getAll

}
