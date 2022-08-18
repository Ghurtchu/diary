package route.implementation

import db.*
import model.Note
import route.interface._
import route.interface.SortOrder._
import route.interface.{CanSort, SortOrder}
import zio.*


class NoteSortService extends CanSort[Note] {

  private val notesRepository: CRUD[Note] = NotesRepository()

  final override def sort(sortOrder: SortOrder): Task[List[Note]] = for {
    notes  <- notesRepository.getAll
    sorted <- ZIO.succeed {
      sortOrder match
        case Ascending  => notes.sortWith(_.title < _.title)
        case Descending => notes.sortWith(_.title > _.title)
    }
  } yield sorted
}
