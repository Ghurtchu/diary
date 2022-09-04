package route.handler

import model.{JwtContent, Note}
import zhttp.http.Response
import zhttp.http.Request
import util.sort.{SortNoteService, SortOrder, SortService}
import zio.json.*
import zio.*

trait SortNoteHandler {
  def handle(request: Request, jwtContent: JwtContent): Task[Response]
}

final case class SortNoteHandlerLive(sortNoteService: SortService[Note]) extends SortNoteHandler {

  final override def handle(request: Request, jwtContent: JwtContent): Task[Response] = for {
    queryParams <- ZIO.succeed(request.url.queryParams)
    sortOrder   <- getSortOrderFromQueryParams(queryParams)
    ordered     <- sortNoteService.sort(sortOrder, jwtContent.id)
    response    <- ZIO.succeed(Response.text(ordered.toJsonPretty))
  } yield response

  private def getSortOrderFromQueryParams(queryParams: Map[String, List[String]]) = ZIO.succeed {
      queryParams
        .get("order")
        .fold(SortOrder.Ascending)(ord =>if ord.head == "asc" then SortOrder.Ascending else SortOrder.Descending)
    }
  
}

object SortNoteHandlerLive {
  lazy val layer: URLayer[SortService[Note], SortNoteHandler] = ZLayer.fromFunction(SortNoteHandlerLive.apply)
}
