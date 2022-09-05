package route.interface

import zhttp.http.Response
import zio.Task

trait DeleteNoteService:
  def deleteRecord(noteId: Long, userId: Long): Task[Either[String, String]]
