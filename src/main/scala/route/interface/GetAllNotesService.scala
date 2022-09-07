package route.interface

import model.Note
import zhttp.http.Response
import zio.{Task, UIO}

trait GetAllNotesService:
  
  def getNotesByUserId(userId: Long): Task[List[Note]]

