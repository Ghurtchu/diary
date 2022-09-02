package route.implementation

import db.{NotesRepository, NotesRepositoryLive}
import model.Note
import route.interface.{CreateNoteService, UpdateNoteService}
import zhttp.http.Response
import zio.*
import zio.json.*

final case class UpdateNoteServiceLive(notesRepository: NotesRepository) extends UpdateNoteService {
  
  override def updateNote(id: Long, note: Note): Task[Either[String, String]] = for {
    updateStatus <- notesRepository.update(id, note)
  } yield updateStatus

}

object UpdateNoteServiceLive {
  
  lazy val layer: URLayer[NotesRepository, UpdateNoteService] = ZLayer.fromFunction(UpdateNoteServiceLive.apply _)

}
