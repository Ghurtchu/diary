package route.service

import db.NotesRepository
import model.Note
import route.interface.CanCreateRecord
import zhttp.http.Response
import zio.*
import zio.json.*

class CreateNoteService extends CanCreateRecord {

  private val notesRepository: NotesRepository.type = NotesRepository

  override def serve(recordAsJon: String): Task[Either[String, String]] = for {
    noteEither     <- ZIO.succeed(recordAsJon.fromJson[Note])
    note           <- noteEither.fold(err => ZIO.fail(new RuntimeException(err)), ZIO.succeed(_))
    creationStatus <- notesRepository add note
    response       <- ZIO.succeed {
      if creationStatus then Right(s"Note with title ${note.title} was created successfully")
      else Left(s"Note with title ${note.title} was not created")
    }
  } yield response

}
