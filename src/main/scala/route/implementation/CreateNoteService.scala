package route.implementation

import db.{CRUD, NotesRepository}
import model.Note
import route.interface.CanCreateRecord
import zhttp.http.Response
import zio.*
import zio.json.*

class CreateNoteService extends CanCreateRecord[Note] {

  private val notesRepository: CRUD[Note] = NotesRepository()

  override def createRecord(note: Note): Task[Either[String, String]] = for {
    creationStatus <- notesRepository add note
  } yield creationStatus

}
