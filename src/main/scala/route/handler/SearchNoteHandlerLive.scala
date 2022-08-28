package route.handler

import model.*
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

final case class SearchNoteHandlerLive(searchNoteService: SearchService[Note]) extends SearchNoteHandler {

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

object SearchNoteHandlerLive {
  lazy val layer: URLayer[SearchService[Note], SearchNoteHandler] = ZLayer.fromFunction(SearchNoteHandlerLive.apply _)
}

sealed trait SearchCriteria { self =>
  def isExact: Boolean
  def fold[A](ifExact: => A)(ifNonExact: => A): A = self match
      case Exact    => ifExact
      case NonExact => ifNonExact
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
