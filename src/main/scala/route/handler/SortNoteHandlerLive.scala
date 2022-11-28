package route.handler

import zhttp.http.Response
import zhttp.http.Request

import sort.{SortNoteService, SortOrder, SortService}
import zio.json.*
import zio.*
import RequestHandlerDefinitions.SortNoteHandler
import domain.Domain.{JwtContent, Note}

final case class SortNoteHandlerLive(sortNoteService: SortService[Note]) extends SortNoteHandler:

  override def handle(request: Request, jwtContent: JwtContent): Task[Response] = 
    for
      queryParams <- ZIO.succeed(request.url.queryParams)
      sortOrder   <- getSortOrderFromQueryParams(queryParams)
      ordered     <- sortNoteService.sort(sortOrder, jwtContent.userId)
      response    <- ZIO.succeed(Response.text(ordered.toJsonPretty))
    yield response

  private def getSortOrderFromQueryParams(queryParams: Map[String, List[String]]) = ZIO.succeed {
      queryParams
        .get("order")
        .fold(SortOrder.Ascending)(ord =>if ord.head == "asc" then SortOrder.Ascending else SortOrder.Descending)
    }


object SortNoteHandlerLive:
  
  lazy val layer: URLayer[SortService[Note], SortNoteHandler] = ZLayer.fromFunction(SortNoteHandlerLive.apply)

