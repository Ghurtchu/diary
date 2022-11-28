package scala.sort

import db.*
import db.note.NotesRepository
import domain.Domain.Note
import zio.*

import scala.sort.SortNoteService.{ascending, descending}

final case class SortNoteService(notesRepository: NotesRepository) extends SortService[Note]:

  def sort(sortOrder: SortOrder, userId: Long): Task[List[Note]] = 
    for 
      notes  <- notesRepository getNotesByUserId userId
      sorted <- ZIO.succeed(sortOrder.fold(notes.sortWith(ascending))(notes.sortWith(descending)))
    yield sorted


object SortNoteService:

    val ascending: (Note, Note) => Boolean = _.title < _.title
    val descending: (Note, Note) => Boolean = _.title > _.title

    lazy val layer: URLayer[NotesRepository, SortNoteService] = ZLayer.fromFunction(SortNoteService.apply)

