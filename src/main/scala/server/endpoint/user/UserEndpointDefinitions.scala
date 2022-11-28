package server.endpoint.user

import zhttp.http.HttpApp

object UserEndpointDefinitions:

  trait LoginEndpoint:
    def route: HttpApp[Any, Throwable]

  trait SignupEndpoint:
    def route: HttpApp[Any, Throwable]