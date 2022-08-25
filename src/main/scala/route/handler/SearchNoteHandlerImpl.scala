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

trait SearchNoteHandler {
  def handle(request: Request): Task[Response]
}

final case class SearchNoteHandlerImpl(searchNoteService: SearchService[Note]) extends SearchNoteHandler {

  final override def handle(request: Request): Task[Response] = for {
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

object SearchNoteHandlerImpl {
  lazy val layer: URLayer[SearchService[Note], SearchNoteHandlerImpl] = ZLayer.fromFunction(SearchNoteHandlerImpl.apply _)
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
