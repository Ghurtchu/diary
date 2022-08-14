package route.implementation

import model.*
import route.interface.HasQueryParam
import service.*
import service.search.{NotesSearchService, CanSearch}
import zhttp.http.*
import zio.*
import zio.json.*

import java.time.Instant
import java.util.Date


class SearchNoteRoute extends HasQueryParam {

  private val searchService: CanSearch[Note] = NotesSearchService

  override def handle(title: String): Task[Response] =
    searchService.searchByTitle(title)
      .map(_.fold(_.toNotFoundResponse, buildJsonResponse))

  private def buildJsonResponse(note: Note): Response = Response.text(note.toJsonPretty)
    
}
