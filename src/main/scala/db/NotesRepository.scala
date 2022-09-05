package db

import db.Repository.DeletionStatus
import model.Note
import zio.Task

trait NotesRepository extends Repository[Note]:

  def getAll: Task[List[Note]]
  
  def getNotesByUserId(userId: Long): Task[List[Note]]

  def getNoteByIdAndUserId(noteId: Long, userId: Long): Task[Option[Note]]

  def deleteNoteByIdAndUserId(noteId: Long, userId: Long): Task[Either[DbError, String]]
