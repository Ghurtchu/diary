package route.interface

import zhttp.http.Request
import zio._
import zhttp.http.Response

trait CommonRequestHandler[A] {
  def handle(param: A): Task[Response]
}
