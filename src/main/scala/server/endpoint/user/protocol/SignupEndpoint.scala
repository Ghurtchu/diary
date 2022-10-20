package server.endpoint.user.protocol

import zhttp.http.HttpApp

trait SignupEndpoint:
  def route: HttpApp[Any, Throwable]
