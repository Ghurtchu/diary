package route.interface

import zhttp.http.Response
import zio.Task

trait CanUpdateRecord[A, B] {
  def handle(id: A, updated: B): Task[Response]
}
