package route.interface

import zhttp.http.Response
import zio.UIO

trait CanRetrieveRecords[A] {
  def retrieveRecords: UIO[List[A]]
}
