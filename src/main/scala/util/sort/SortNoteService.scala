package util.sort

import db.*
import model.Note
import route.interface.*
import util.sort.SortOrder.*
import util.sort.{SortOrder, SortService}
import zio.*


final case class SortNoteService(notesRepository: CRUD[Note]) extends SortService[Note] {

  final def sort(sortOrder: SortOrder): Task[List[Note]] = for {
    notes  <- notesRepository.getAll
    sorted <- ZIO.succeed(sortOrder.fold(notes.sortWith(_.title < _.title))(notes.sortWith(_.title > _.title)))
  } yield sorted
}

object SortNoteService {
 lazy val layer: URLayer[CRUD[Note], SortNoteService] = ZLayer.fromFunction(SortNoteService.apply _)
}
