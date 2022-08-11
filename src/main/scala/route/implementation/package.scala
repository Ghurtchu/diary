package route

import zhttp.http.Response
import zhttp.http.Status

package object implementation {
  
    extension (eitherStringString: Either[String, String])
      def toResponse: Response = eitherStringString.fold(
        Response.text(_).setStatus(Status.NotFound),
        Response.text(_).setStatus(Status.Ok))

    extension(message: String)
      def toResponse: Response = Left(message).toResponse
}
