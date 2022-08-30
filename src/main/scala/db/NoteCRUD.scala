package db

import db.CRUD.DeletionStatus
import model.Note
import zio._

trait NoteCRUD extends CRUD[Note]{
  def getNotesByUserId(userId: Int): UIO[List[Note]]
  def getNoteByIdAndUserId(noteId: Int, userId: Int): UIO[Option[Note]]
  def deleteNoteByIdAndUserId(noteId: Int, userId: Int): Task[DeletionStatus]
}
