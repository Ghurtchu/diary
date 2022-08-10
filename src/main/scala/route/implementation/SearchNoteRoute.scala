package route.implementation

import model.*
import route.interface.HasQueryParam
import service.*
import service.search.{NotesSearchService, SearchService}
import zhttp.http.*
import zio.*
import zio.json.*

import java.time.Instant
import java.util.Date


class SearchNoteRoute extends HasQueryParam {

  private val searchService: SearchService[Note] = NotesSearchService

  override def handle(title: String): Task[Response] =
    searchService.searchByTitle(title)
      .fold(
        _ => Response.text("Error occurred while searching"),
        maybeNote => maybeNote.fold(
          Response.text(s"Note with title $title does not exist"))
        (note => Response.text(note.toJsonPretty)))
}
