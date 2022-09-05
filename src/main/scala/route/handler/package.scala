package route

import db.DbError._
import zhttp.http.Response
import zhttp.http.Status
import zio.json.*

package object handler {

    extension [A](elems: List[A])
      def toJsonResponse(using jsonEncoder: JsonEncoder[A]): Response = Response.text(elems.toJson)

    extension [A](eitherNotFoundOrFound: Either[NotFound, A])
      def toResponse(using jsonEncoder: JsonEncoder[A]): Response = eitherNotFoundOrFound.fold(
        notFound => Response.text(notFound.msg).setStatus(Status.NotFound),
        elem     => Response.text(elem.toJson).setStatus(Status.Ok)
      )
  
    extension[A] (any: A)(using jsonEncoder: JsonEncoder[A])
        def toJsonResponse: Response = Response.text(any.toJsonPretty)
}
