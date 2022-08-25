package route.handler

import model.Note
import zhttp.http.Response
import zhttp.http.Request
import util.sort.{SortNoteService, SortOrder, SortService}
import zio.json.*
import zio._

trait SortNoteHandler {
  def handle(request: Request): Task[Response]
}

final case class SortNoteHandlerImpl(sortNoteService: SortService[Note]) extends SortNoteHandler {

  final override def handle(request: Request): Task[Response] = for {
    sortOrder       <- ZIO.succeed {
      val queryParamsMap = request.url.queryParams

      queryParamsMap.get("order")
        .fold(SortOrder.Ascending) { ord =>
          if ord.head == "asc" then SortOrder.Ascending else SortOrder.Descending
        }
    }
    ordered         <- sortNoteService.sort(sortOrder)
    response        <- ZIO.succeed(Response.text(ordered.toJsonPretty))
  } yield response

}

object SortNoteHandlerImpl {
  lazy val layer: URLayer[SortService[Note], SortNoteHandlerImpl] = ZLayer.fromFunction(SortNoteHandlerImpl.apply _)
}
