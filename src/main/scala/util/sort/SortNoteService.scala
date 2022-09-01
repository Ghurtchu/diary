package util.sort

import db.*
import db.note.NoteCRUD
import model.Note
import route.interface.*
import util.sort.SortOrder.*
import util.sort.{SortOrder, SortService}
import zio.*


final case class SortNoteService(notesRepository: NoteCRUD) extends SortService[Note] {

  final def sort(sortOrder: SortOrder, userId: Long): Task[List[Note]] = for {
    notes  <- notesRepository getNotesByUserId userId
    sorted <- ZIO.succeed(sortOrder.fold(notes.sortWith(_.title < _.title))(notes.sortWith(_.title > _.title)))
  } yield sorted
}

object SortNoteService {
 lazy val layer: URLayer[NoteCRUD, SortNoteService] = ZLayer.fromFunction(SortNoteService.apply _)
}
