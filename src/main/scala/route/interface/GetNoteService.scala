package route.interface

import model.Note
import zio.*

trait GetNoteService {
  def getNote(noteId: Int, userId: Int): Task[Either[String, Note]]
}
