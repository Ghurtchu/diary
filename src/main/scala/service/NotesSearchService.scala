package service
import zio.Task

import model._

object NotesSearchService extends SearchService[Note] {

  override def searchByTitle(title: String): Task[Option[Any]] = ZIO.attempt {
    Note(1, "first note", "first note body", Date.from(Instant.now()).toString, User(1, "Nika", "Ghurtchumelia"))
  }

}
