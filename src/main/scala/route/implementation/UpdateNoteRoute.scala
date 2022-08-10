package route.implementation

import model.Note
import route.interface.CanUpdateRecord
import service.NotesService
import zhttp.http.Response
import zio.*
import zio.json.*

class UpdateNoteRoute extends CanUpdateRecord[Int] {

  private val notesService: NotesService.type    = NotesService

  override def handle(id: Int, updatedNoteAsJson: String): Task[Response] = for {
    noteEither <- ZIO.succeed(updatedNoteAsJson.fromJson[Note])
    status     <- noteEither.fold(err => ZIO.fail(RuntimeException(err)), note => notesService.update(id, note))
    response   <- ZIO.succeed { 
      if status then Response.text("Note was updated successfully")
      else Response.text("Note was not updated")
    }
  } yield response


}
