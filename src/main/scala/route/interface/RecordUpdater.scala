package route.interface

import zhttp.http.Response
import zio.Task

trait RecordUpdater[A] {
  def updateRecord(id: Int, newRecord: A): Task[Either[String, String]]
}
