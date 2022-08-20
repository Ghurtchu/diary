package route.interface

import zhttp.http.Request
import zio._
import zhttp.http.Response

trait CommonRequestHandler[Service, A] {
  def handle(param: A): ZIO[Service, Throwable, Response]
}
