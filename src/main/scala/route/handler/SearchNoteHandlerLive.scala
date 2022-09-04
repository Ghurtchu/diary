package route.handler

import zhttp.http.Request
import model.*
import util.*
import util.search.{SearchNoteService, SearchService}
import zhttp.http.*
import zio.*
import zio.json.*

import java.time.Instant
import java.util.Date

trait SearchNoteHandler {
  def handle(request: Request, jwtContent: JwtContent): Task[Response]
}

final case class SearchNoteHandlerLive(searchNoteService: SearchService[Note]) extends SearchNoteHandler {

  final override def handle(request: Request, jwtContent: JwtContent): Task[Response] = for {
    queryParams    <- ZIO.succeed(request.url.queryParams)
    title          <- getTitleFromQueryParams(queryParams)
    searchCriteria <- getSearchCriteriaFromQueryParams(queryParams)
    searchResult   <- searchNoteService.searchByTitle(title, searchCriteria, jwtContent.id)
    response       <- ZIO.succeed(searchResult.fold(Response.text, note => Response.text(note.toJsonPretty)))
  } yield response

  private def getSearchCriteriaFromQueryParams(queryParams: Map[String, List[String]]) = 
    ZIO.succeed {
      queryParams
        .get("exact")
        .fold(SearchCriteria.nonExact)(criteria => if criteria.head == "true" then SearchCriteria.exact else SearchCriteria.nonExact)
    }

  private def getTitleFromQueryParams(queryParams: Map[String, List[String]]): UIO[String] =
    ZIO.succeed {
      queryParams
        .get("title")
        .fold("")(params => if params.isDefinedAt(0) then params.head else "")
    }

}

object SearchNoteHandlerLive {
  lazy val layer: URLayer[SearchService[Note], SearchNoteHandler] = ZLayer.fromFunction(SearchNoteHandlerLive.apply)
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
