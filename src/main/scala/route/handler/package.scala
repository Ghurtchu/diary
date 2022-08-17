package route

import zhttp.http.Response
import zhttp.http.Status
import zio.json._

package object handler {
  
    extension (eitherStringString: Either[String, String])
      def toResponse: Response = eitherStringString.fold(
        Response.text(_).setStatus(Status.NotFound),
        Response.text(_).setStatus(Status.Ok))

    extension(errorMessage: String)
      def toNotFoundResponse: Response = Left(errorMessage).toResponse
      
    extension[A] (any: A)(using jsonEncoder: JsonEncoder[A])
        def toJsonResponse: Response = Response.text(any.toJsonPretty)
}
