package route.interface

import zio._

trait CanRetrieveRecord[A] {
  def serve(id: Int): Task[Either[String, A]]
}
