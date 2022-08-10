package route.interface

import zhttp.http.Response
import zio.UIO

trait CanRetrieveCollection {
  def handle: UIO[Response]
}
