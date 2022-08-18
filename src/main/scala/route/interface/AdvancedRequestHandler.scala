package route.interface

import zhttp.http.Response
import zio._

trait AdvancedRequestHandler[A, B] {
  def handle(a: A, b: B): Task[Response]
}
