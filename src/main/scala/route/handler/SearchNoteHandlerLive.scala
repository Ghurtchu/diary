package route.handler

import zhttp.http.Request
import domain.*

import util.*
import search.{SearchCriteria, SearchNoteService, SearchService}
import zhttp.http.*
import zio.*
import zio.json.*
import RequestHandlerDefinitions.SearchNoteHandler
import domain.Domain.{JwtContent, Note}

import java.time.Instant
import java.util.Date

final case class SearchNoteHandlerLive(searchNoteService: SearchService[Note]) extends SearchNoteHandler:

  override def handle(request: Request, jwtContent: JwtContent): Task[Response] = 
    for
      queryParams    <- ZIO.succeed(request.url.queryParams)
      title          <- getTitleFromQueryParams(queryParams)
      searchCriteria <- getSearchCriteriaFromQueryParams(queryParams)
      searchResult   <- searchNoteService.searchByTitle(title, searchCriteria, jwtContent.userId)
      response       <- ZIO.succeed(searchResult.fold(Response.text, note => Response.text(note.toJsonPretty)))
    yield response

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


object SearchNoteHandlerLive:

  lazy val layer: URLayer[SearchService[Note], SearchNoteHandler] = ZLayer.fromFunction(SearchNoteHandlerLive.apply)

