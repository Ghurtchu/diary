package db.note

import db.Repository
import db.Repository.DBResult
import domain.Domain.Note
import zio.Task

trait NotesRepository extends Repository[Note] :

  def getAll: Task[List[Note]]

  def getNotesByUserId(userId: Long): Task[List[Note]]

  def getNoteByIdAndUserId(noteId: Long, userId: Long): Task[Option[Note]]

  def deleteNoteByIdAndUserId(noteId: Long, userId: Long): Task[DBResult]
