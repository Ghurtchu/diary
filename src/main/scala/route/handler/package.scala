package route

import db.DbError.*
import db.DbResponse
import zhttp.http.Response
import zhttp.http.Status
import zio.json.*

package object handler:

    extension [A](elems: List[A])
      def toJsonResponse(using jsonEncoder: JsonEncoder[A]): Response = Response.text(elems.toJson)

    extension [A](eitherNotFoundOrFound: Either[DbResponse, A])
      def toResponse(using jsonEncoder: JsonEncoder[A]): Response = eitherNotFoundOrFound.fold(
        dbResponse => Response.text(dbResponse.msg).setStatus(Status.NotFound),
        elem       => Response.text(elem.toJson).setStatus(Status.Ok)
      )

    extension[A] (any: A)(using jsonEncoder: JsonEncoder[A])
        def toJsonResponse: Response = Response.text(any.toJsonPretty)

