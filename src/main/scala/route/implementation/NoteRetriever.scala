package route.implementation

import route.interface.NeedsFieldValue
import service.NotesService
import zhttp.http.Response
import zio.*
import zio.json.*

class NoteRetriever extends NeedsFieldValue[Int] {

  private val notesService: NotesService.type = NotesService

  override def handle(field: Int): Task[Response] =
    notesService.getById(field)
      .fold(_ => Response.text("Error occurred while searching"),
        maybeNote => maybeNote.fold(Response.text(s"Note with id $field was not found"))
        (note => Response.text(note.toJsonPretty)))

}
