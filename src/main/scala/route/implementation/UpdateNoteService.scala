package route.implementation

import db.{CRUD, NotesRepository}
import model.Note
import route.interface.{RecordCreator, RecordUpdater}
import zhttp.http.Response
import zio.*
import zio.json.*


class UpdateNoteService(notesRepository: CRUD[Note]) extends RecordUpdater[Note] {

  override def updateRecord(id: Int, note: Note): Task[Either[String, String]] = for {
    updateStatus <- notesRepository.update(id, note)
  } yield updateStatus

}

object UpdateNoteService {
  def spawn(notesRepository: CRUD[Note]): UpdateNoteService = new UpdateNoteService(notesRepository)
  
  def layer: ULayer[UpdateNoteService] = NotesRepository.layer >>> ZLayer.fromFunction(UpdateNoteService.spawn)
}
