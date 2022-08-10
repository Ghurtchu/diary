package route.interface

import zhttp.http.{Request, Response}

trait HasRequestBody {
  def handle(request: Request): Response
}
