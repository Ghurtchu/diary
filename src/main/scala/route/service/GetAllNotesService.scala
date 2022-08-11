package route.service

import db.{CRUD, NotesRepository}
import model.Note
import route.interface.CanRetrieveCollection
import zhttp.http.Response
import zio.*
import zio.json.*

class GetAllNotesService extends CanRetrieveCollection[Note] {

  private val notesRepository: CRUD[Note] = NotesRepository

  override def serve: UIO[List[Note]] = notesRepository.getAll

}
