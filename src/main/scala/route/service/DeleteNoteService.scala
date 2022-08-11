package route.service

import db.NotesRepository
import route.interface.CanDeleteRecord
import zhttp.http.Response
import zio.*

class DeleteNoteService extends CanDeleteRecord {

  private val notesRepository: NotesRepository.type = NotesRepository

  override def serve(id: Int): Task[Either[String, String]] = for {
    deleteStatus <- notesRepository delete id
    response     <- ZIO.succeed {
      if deleteStatus then Right(s"Note with id $id was deleted successfully")
      else Left(s"Note with id $id was not deleted")
    }
  } yield response

}
