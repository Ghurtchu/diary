package server.endpoint.user.protocol

import zhttp.http.HttpApp

trait LoginEndpoint:
  def route: HttpApp[Any, Throwable]
