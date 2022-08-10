package route.interface

import zhttp.http.Response
import zio.Task

trait NeedsFieldValue[A] {
  def handle(field: A): Task[Response]
}
