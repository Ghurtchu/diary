package route.interface

import zhttp.http.Response
import zio.Task

trait CanDeleteRecord {
  def deleteRecord(id: Int): Task[Either[String, String]]
}