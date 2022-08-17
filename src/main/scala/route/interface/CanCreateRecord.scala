package route.interface

import zhttp.http.Response
import zio.Task
import db.CRUD

trait CanCreateRecord[A] {
  def createRecord(record: A): Task[Either[String, String]]
}
