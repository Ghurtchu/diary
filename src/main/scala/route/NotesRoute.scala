package route

import db.CRUD
import service.*

import java.util.Date
import java.time.Instant
import model.*
import service.*
import service.search.{NotesSearchService, SearchService}
import zio.*
import zhttp.http.*
import zio.json.EncoderOps

object NotesRoute {

  val notesService: NotesService.type    = NotesService
  val searchService: SearchService[Note] = NotesSearchService

  def getAllNotes: UIO[Response] = for {
    notes    <- notesService.getAll
    response <- ZIO.succeed(Response.text(notes.toJsonPretty))
  } yield response

  def getNoteById(id: Int): Task[Response] =
    notesService.getById(id)
      .fold(_ => Response.text("not found"),
            note => Response.json(note.toJsonPretty))


  def searchByTitle(title: String): Task[Response] =
    val cleanedTitle = title.replace("%20", " ")
    searchService.searchByTitle(cleanedTitle)
      .fold(
        _ => Response.text("Error occurred while searching"),
        maybeNote => maybeNote.fold(
          Response.text("{}"))
        (note => Response.text(note.toJsonPretty)))


}
