package route.interface

import zhttp.http.Response
import zio.UIO

trait CanRetrieveResponse {
  def handle: UIO[Response]
}
