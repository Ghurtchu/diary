package service
import zio._

import model._
import java.util.Date
import java.time.Instant

object NotesSearchService extends SearchService[Note] {

  override def searchByTitle(title: String): Task[Option[Note]] = ZIO.attempt {
    Some(Note(1, "first note", "first note body", Date.from(Instant.now()).toString, User(1, "Nika", "Ghurtchumelia")))
  }
  
}
