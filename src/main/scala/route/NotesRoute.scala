package route

import java.util.Date
import java.time.Instant
import model.*
import service._
import zio.*
import zhttp.http.*
import zio.json.EncoderOps

object NotesRoute {

  val notesService: CRUD[Note] = NotesService
  val searchService: SearchService[Note] = NotesSearchService

  def notes(): UIO[Response] = ZIO.succeed {
    Response.text(List(
      Note(1, "first note", "first note body...", Date.from(Instant.now()).toString, User(1, "Nika", "Ghurtchumelia")),
      Note(2, "second note", "second note body...", Date.from(Instant.now()).toString, User(2, "Ozzy", "Osbourne")),
      Note(3, "third note", "third note body...", Date.from(Instant.now()).toString, User(3, "Tony", "Iommi"))
    ).toJsonPretty)
  }

  def note(id: Int): Task[Response] =
    notesService.getById(id)
      .fold(_ => Response.text("not found"),
            note => Response.json(note.toJsonPretty))


  def search(title: String): Task[Response] =
    searchService.searchByTitle(title)
      .fold(_ => Response.text("not found"),
        note => Response.json(note.toJsonPretty))


}
