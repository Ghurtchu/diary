package route.interface

import zhttp.http.Response
import zio.UIO

trait CanRetrieveCollection[A] {
  def serve: UIO[List[A]]
}
