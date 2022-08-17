package route.interface

import zio._

trait CanRetrieveRecord[A] {
  def retrieveRecord(id: Int): Task[Either[String, A]]
}
