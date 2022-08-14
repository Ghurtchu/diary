package route.service

import db.NotesRepository
import model.Note
import route.interface.{CanCreateRecord, CanUpdateRecord}
import zhttp.http.Response
import zio.*
import zio.json.*


class UpdateNoteService extends CanUpdateRecord {

  private val notesRepository: NotesRepository.type = NotesRepository

  override def serve(id: Int, newRecordAsJson: String): Task[Either[String, String]] = for {
    noteEither   <- ZIO.succeed(newRecordAsJson.fromJson[Note])
    updateStatus <- noteEither.fold(err => ZIO.fail(RuntimeException(err)), note => notesRepository.update(id, note))
    response     <- ZIO.succeed {
      if updateStatus then Right(s"Note with id ${id.withQuotes} was updated successfully")
      else Left("Note was not updated")
    }
  } yield response

}
