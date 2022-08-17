package route.handler

import model.*
import service.*
import service.search.{NotesSearchService, CanSearch}
import zhttp.http.*
import zio.*
import zio.json.*

import java.time.Instant
import java.util.Date

class SearchNoteRoute {

  private val searchService: CanSearch[Note] = NotesSearchService

  def handle(request: Request): Task[Response] = for {
    title        <- ZIO.succeed(request.url.queryParams("title").head)
    searchResult <- searchService searchByTitle title
    response     <- ZIO.succeed(searchResult.fold(
      err  => Response.text(err),
      note => Response.text(note.toJsonPretty)
    ))
  } yield response
  
    
}
