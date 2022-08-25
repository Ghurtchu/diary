package route.implementation

import db._
import model.Note
import route.interface.RecordsRetriever
import zhttp.http.Response
import zio.*
import zio.json.*

final case class GetAllNotesService private(notesRepository: CRUD[Note]) extends RecordsRetriever[Note] {

  override def retrieveRecords: UIO[List[Note]] = 
    notesRepository.getAll

}

object GetAllNotesService {

  lazy val layer: URLayer[CRUD[Note], GetAllNotesService] =
    ZLayer.fromFunction(GetAllNotesService.apply _)
  
}
