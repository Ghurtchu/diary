package route.implementation

import db.{CRUD, NotesRepository}
import model.Note
import route.interface.{RecordCreator, RecordUpdater}
import zhttp.http.Response
import zio.*
import zio.json.*


final case class UpdateNoteService(notesRepository: CRUD[Note]) extends RecordUpdater[Note] {

  override def updateRecord(id: Int, note: Note): Task[Either[String, String]] = for {
    updateStatus <- notesRepository.update(id, note)
  } yield updateStatus

}

object UpdateNoteService {
  
  lazy val layer: URLayer[CRUD[Note], UpdateNoteService] = ZLayer.fromFunction(UpdateNoteService.apply _)

}
