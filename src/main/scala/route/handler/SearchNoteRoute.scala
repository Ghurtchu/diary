package route.handler

import model.*
import route.interface.CommonRequestHandler
import util.*
import util.search.{SearchNoteService, SearchService}
import zhttp.http.*
import zio.*
import zio.json.*

import java.time.Instant
import java.util.Date

class SearchNoteRoute extends CommonRequestHandler[Request, SearchService[Note]] {

  final override def handle(request: Request): RIO[SearchService[Note], Response] = for {
    searchNoteService <- ZIO.service[SearchService[Note]]
    title             <- ZIO.succeed(request.url.queryParams("title").head)
    searchCriteria    <- ZIO.succeed {
      request.url.queryParams.get("exact")
        .fold(SearchCriteria.nonExact)(criteria => if criteria.head == "true" then SearchCriteria.exact else SearchCriteria.nonExact)
    }
    searchResult      <- searchNoteService.searchByTitle(title, searchCriteria)
    response          <- ZIO.succeed(searchResult.fold(
      err  => Response.text(err),
      note => Response.text(note.toJsonPretty)
    ))
  } yield response

}

sealed trait SearchCriteria {
  def isExact: Boolean
}

object SearchCriteria {
  def exact: SearchCriteria = Exact

  def nonExact: SearchCriteria = NonExact
}

case object Exact extends SearchCriteria {
  final override def isExact: Boolean = true
}

case object NonExact extends SearchCriteria {
  final override def isExact: Boolean = false
}
