package route.interface

import zhttp.http.Response
import zio.Task

trait RecordRemover {
  def deleteRecord(id: Int): Task[Either[String, String]]
}