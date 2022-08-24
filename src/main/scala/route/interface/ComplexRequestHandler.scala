package route.interface

import zhttp.http.Response
import zio._

trait ComplexRequestHandler[A, B, Env] {
  def handle(a: A, b: B): RIO[Env, Response]
}
