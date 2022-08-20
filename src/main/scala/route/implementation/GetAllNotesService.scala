package route.implementation

import db._
import model.Note
import route.interface.RecordsRetriever
import zhttp.http.Response
import zio.*
import zio.json.*

class GetAllNotesService private(notesRepository: CRUD[Note]) extends RecordsRetriever[Note] {

  override def retrieveRecords: UIO[List[Note]] = 
    notesRepository.getAll

}

object GetAllNotesService {
  def spawn(notesRepository: CRUD[Note]): GetAllNotesService = new GetAllNotesService(notesRepository)

  def layer: ZLayer[Any, Nothing, GetAllNotesService] = NotesRepository.layer >>> ZLayer.fromFunction(GetAllNotesService.spawn)
}
