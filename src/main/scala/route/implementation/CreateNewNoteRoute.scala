package route.implementation

import model.Note
import route.interface.CanCreateRecord
import service.NotesService
import zhttp.http.Response
import zio.*
import zio.json.*

class CreateNewNoteRoute extends CanCreateRecord[String] {

  private val notesService: NotesService.type = NotesService

  override def handle(noteAsString: String): Task[Response] = for {
    noteEither <- ZIO.succeed(noteAsString.fromJson[Note])
    status <- noteEither.fold(err => ZIO.fail(RuntimeException(err)), notesService.add)
    response <- ZIO.succeed {
      if status then Response.text(s"Note with title ${noteEither.map(_.title)} was added successfully")
      else Response.text(s"Note with title ${noteEither.map(_.title)} was not added")
    }
  } yield response

}
