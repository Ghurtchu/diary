package route.handler

import model.Note
import route.interface.{CanSort, CommonRequestHandler, SortOrder}
import zhttp.http.Response
import zio.{Task, ZIO}
import zhttp.http.Request
import route.implementation.SortNoteService
import zio.json._

class SortNoteRoute extends CommonRequestHandler[Request] {

  val sortService: CanSort[Note] = SortNoteService()

  final override def handle(request: Request): Task[Response] = for {
    sortOrder <- ZIO.succeed {
      val queryParamsMap = request.url.queryParams

      queryParamsMap.get("order")
        .fold(SortOrder.Ascending) { ord =>
          if ord.head == "asc" then SortOrder.Ascending else SortOrder.Descending
        }
    }
    ordered  <- sortService.sort(sortOrder)
    response <- ZIO.succeed(Response.text(ordered.toJsonPretty))
  } yield response
}
