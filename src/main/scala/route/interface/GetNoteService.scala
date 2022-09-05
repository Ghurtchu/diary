package route.interface

import db.{DbError, DbResponse}
import model.Note
import zio.*

trait GetNoteService:
  def getNote(noteId: Long, userId: Long): Task[Either[DbResponse, Note]]

