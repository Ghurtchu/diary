package route.service

import db.{CRUD, NotesRepository}
import model.Note
import route.interface.{CanCreateRecord, CanUpdateRecord}
import zhttp.http.Response
import zio.*
import zio.json.*


class UpdateNoteService extends CanUpdateRecord[Note] {

  private val notesRepository: CRUD[Note] = NotesRepository()

  override def updateRecord(id: Int, note: Note): Task[Either[String, String]] = for {
    updateStatus <- notesRepository.update(id, note)
  } yield updateStatus

}
