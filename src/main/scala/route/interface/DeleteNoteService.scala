package route.interface

import zhttp.http.Response
import zio.Task

trait DeleteNoteService {
  def deleteRecord(noteId: Int, userId: Int): Task[Either[String, String]]
}