package route

import db.DbError.*
import zhttp.http.Response
import zhttp.http.Status
import zio.json.*

package object handler:

    extension [A](elems: List[A])
      def toJsonResponse(using jsonEncoder: JsonEncoder[A]): Response = Response.text(elems.toJson)

    extension [A](eitherNotFoundOrFound: Either[String, A])
      def notFoundOrFound(using jsonEncoder: JsonEncoder[A]): Response = eitherNotFoundOrFound.fold(
        Response.text(_).setStatus(Status.NotFound),
        record => Response.text(record.toJson).setStatus(Status.Ok)
      )

    extension[A] (any: A)(using jsonEncoder: JsonEncoder[A])
        def toJsonResponse: Response = Response.text(any.toJsonPretty)

