package route.interface

import zhttp.http.Response
import zio.Task
import model.Note

trait CreateNoteService:
  def createNote(note: Note): Task[Either[String, String]]

