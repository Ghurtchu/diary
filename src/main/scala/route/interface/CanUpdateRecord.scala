package route.interface

import zhttp.http.Response
import zio.Task

trait CanUpdateRecord[A] {
  def handle(id: A, updatedRecordAsJson: String): Task[Response]
}
