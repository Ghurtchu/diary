package route.interface

import zio._
import zhttp.http._

trait CommonRequestHandler[A, Env] {
  def handle(a: A): RIO[Env, Response]
}
