package route.interface

import zhttp.http.Response
import zio.UIO

trait RecordsRetriever[A] {
  def retrieveRecords: UIO[List[A]]
}
