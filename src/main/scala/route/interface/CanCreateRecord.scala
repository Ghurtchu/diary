package route.interface

import zhttp.http.Response
import zio.Task

trait CanCreateRecord[T] {
  def handle(newRecord: T): Task[Response]
}
