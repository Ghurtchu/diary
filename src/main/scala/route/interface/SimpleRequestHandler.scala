package route.interface

import zio._
import zhttp.http.Response

trait SimpleRequestHandler[Service] {
  def handle: RIO[Service, Response]
}
