package route.interface

import model.Note
import zhttp.http.Response
import zio.Task

trait UpdateNoteService {
  def updateNote(id: Long, newNote: Note): Task[Either[String, String]]
}
