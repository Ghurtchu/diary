package route.interface

import zhttp.http.Response
import zio.Task

trait CanUpdateRecord {
  def serve(id: Int, newRecordAsJson: String): Task[Either[String, String]]
}
