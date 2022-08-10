package route.interface

import zhttp.http.Response
import zio.Task

trait CanDeleteRecord[A] {
  def handle(field: A): Task[Response]
}
