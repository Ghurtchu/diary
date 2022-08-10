package route.interface

import zhttp.http.Response
import zio.Task

trait CanCreateNewRecord {
  def handle(newRecordAsJson: String): Task[Response]
}
