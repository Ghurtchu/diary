package route.implementation

import db.*
import model.Note
import route.interface._
import route.interface.SortOrder._
import route.interface.{CanSort, SortOrder}
import zio.*


class SortNoteService(notesRepository: CRUD[Note]) extends CanSort[Note] {

  final def sort(sortOrder: SortOrder): Task[List[Note]] = for {
    notes  <- notesRepository.getAll
    sorted <- ZIO.succeed {
      sortOrder match
        case Ascending  => notes.sortWith(_.title < _.title)
        case Descending => notes.sortWith(_.title > _.title)
    }
  } yield sorted
}

object SortNoteService {
  def spawn(notesRepository: CRUD[Note]): SortNoteService = new SortNoteService(notesRepository)
  
  def layer: ZLayer[Any, Nothing, CanSort[Note]] = NotesRepository.layer >>> ZLayer.fromFunction(SortNoteService.spawn)
}
