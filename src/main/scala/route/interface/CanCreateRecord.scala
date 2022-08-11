package route.interface

import zhttp.http.Response
import zio.Task

trait CanCreateRecord {
  def serve(recordAsJon: String): Task[Either[String, String]]
}
