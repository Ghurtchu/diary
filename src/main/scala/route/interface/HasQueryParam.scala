package route.interface

import zhttp.http.Response
import zio.Task

trait HasQueryParam {
  def handle(param: String): Task[Response]
}
