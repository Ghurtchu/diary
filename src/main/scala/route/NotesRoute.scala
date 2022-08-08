package route

import java.util.Date
import java.time.Instant

import model._
import service.{CRUD, NotesService}
import zio.*
import zhttp.http.*
import zio.json.EncoderOps

object NotesRoute {

  val notesService: CRUD[Note] = NotesService

  def notes(): UIO[Response] = ZIO.succeed {
    Response.text(List(
      Note(1, "first note", "first note body...", Date.from(Instant.now()).toString, User(1, "Nika", "Ghurtchumelia")),
      Note(2, "second note", "second note body...", Date.from(Instant.now()).toString, User(2, "Ozzy", "Osbourne")),
      Note(3, "third note", "third note body...", Date.from(Instant.now()).toString, User(3, "Tony", "Iommi"))
    ).toJsonPretty)
  }

  def note(id: Int): Task[Response] =
    notesService.getById(id)
      .foldZIO(_ => ZIO.succeed(Response.text("not found")),
            note => ZIO.succeed(Response.json(note.toJsonPretty)))
  
}
