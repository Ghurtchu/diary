package route.handler

import model.Note
import route.interface.{CanSort, CommonRequestHandler, SortOrder}
import zhttp.http.Response
import zio.{RIO, Task, ZIO}
import zhttp.http.Request
import route.implementation.SortNoteService
import zio.json.*

class SortNoteRoute extends CommonRequestHandler[Request, CanSort[Note]] {

  final override def handle(request: Request): RIO[CanSort[Note], Response] = for {
    sortNoteService <- ZIO.service[CanSort[Note]]
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
