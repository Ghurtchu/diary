package server.endpoint.note

import zhttp.http.HttpApp

trait HasRoute[Env]:
  def route: HttpApp[Env, Throwable]

