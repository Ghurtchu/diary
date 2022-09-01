package db.note

import db.CRUD
import db.CRUD.DeletionStatus
import model.Note
import zio.{Task, UIO}

trait NoteCRUD extends CRUD[Note] {

  def getNotesByUserId(userId: Long): UIO[List[Note]]

  def getNoteByIdAndUserId(noteId: Long, userId: Long): UIO[Option[Note]]

  def deleteNoteByIdAndUserId(noteId: Long, userId: Long): Task[DeletionStatus]

}
