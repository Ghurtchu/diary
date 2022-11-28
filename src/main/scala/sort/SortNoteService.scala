package scala.sort

import db.*
import db.note.NotesRepository
import model.Note
import zio._

final case class SortNoteService(notesRepository: NotesRepository) extends SortService[Note]:

  def sort(sortOrder: SortOrder, userId: Long): Task[List[Note]] = 
    for 
      notes  <- notesRepository getNotesByUserId userId
      sorted <- ZIO.succeed(sortOrder.fold(notes.sortWith(_.title < _.title))(notes.sortWith(_.title > _.title)))
    yield sorted


object SortNoteService:
  
    lazy val layer: URLayer[NotesRepository, SortNoteService] = ZLayer.fromFunction(SortNoteService.apply)

