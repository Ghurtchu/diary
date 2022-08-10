package route.implementation

import route.interface.CanDeleteRecord
import service.NotesService
import zhttp.http.Response
import zio.*

class DeleteNoteRoute extends CanDeleteRecord[Int] {

  val notesService: NotesService.type    = NotesService

  override def handle(id: Int): Task[Response] = for {
    deleteStatus <- notesService delete id
    response <- ZIO.succeed {
      if deleteStatus then Response.text(s"Note with id $id was deleted successfully")
      else Response.text(s"Note with id $id does not exist")
    }
  } yield response
}
