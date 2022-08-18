package route.interface

import zio._
import zhttp.http.Response

trait BasicRequestHandler {
  def handle: Task[Response]
}
