package route.interface

import zio._
import zhttp.http.Response

trait BasicRequestHandler[Service] {
  def handle: RIO[Service, Response]
}
