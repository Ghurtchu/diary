package route.interface

import zhttp.http.Response
import zio.Task

trait CanDeleteRecord {
  def serve(id: Int): Task[Either[String, String]]
}
