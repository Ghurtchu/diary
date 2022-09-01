package route.implementation

import db.note.{NoteCRUD, NotesRepository}
import db.CRUD
import model.Note
import route.interface.GetNoteService
import zhttp.http.Response
import zio.*
import zio.json.*

final case class GetNoteServiceLive(private final val notesRepository: NoteCRUD) extends GetNoteService {

  override def getNote(noteId: Long, userId: Long): Task[Either[String, Note]] = for {
    maybeNote <- notesRepository.getNoteByIdAndUserId(noteId, userId)
    response  <- ZIO.succeed(maybeNote.fold(Left(s"Could not find the note with id ${noteId.withQuotes}"))(Right(_)))
  } yield response
}

object GetNoteServiceLive {
  
  lazy val layer: URLayer[NoteCRUD, GetNoteService] = 
    ZLayer.fromFunction(GetNoteServiceLive.apply _)
  
}