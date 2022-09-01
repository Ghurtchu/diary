package route.implementation

import db.*
import db.note.NoteCRUD
import model.Note
import route.interface.GetAllNotesService
import zhttp.http.Response
import zio.*
import zio.json.*

final case class GetAllNotesServiceLive private(notesRepository: NoteCRUD) extends GetAllNotesService {

  override def getNotesByUserId(userId: Long): Task[List[Note]] = notesRepository getNotesByUserId userId

}

object GetAllNotesServiceLive {

  lazy val layer: URLayer[NoteCRUD, GetAllNotesService] =
    ZLayer.fromFunction(GetAllNotesServiceLive.apply _)
  
}
