package route.interface

import zio._

trait RecordRetriever[A] {
  def retrieveRecord(id: Int): Task[Either[String, A]]
}
