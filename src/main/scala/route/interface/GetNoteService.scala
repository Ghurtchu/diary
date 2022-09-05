package route.interface

import db.DbError
import model.Note
import zio.*

trait GetNoteService {
  def getNote(noteId: Long, userId: Long): Task[Either[DbError.NotFound, Note]]
}
