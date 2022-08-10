package route.implementation

import model.Note
import route.interface.CanCreateNewRecord
import service.NotesService
import zhttp.http.Response
import zio.*
import zio.json.*

class CreateNewNoteRoute extends CanCreateNewRecord {

  private val notesService: NotesService.type = NotesService

  override def handle(noteAsJson: String): Task[Response] = for {
    noteEither <- ZIO.succeed(noteAsJson.fromJson[Note])
    status <- noteEither.fold(err => ZIO.fail(RuntimeException(err)), notesService.add)
    response <- ZIO.succeed {
      if status then Response.text(s"Note with title ${noteEither.map(_.title)} was added successfully")
      else Response.text(s"Note with title ${noteEither.map(_.title)} was not added")
    }
  } yield response

}
