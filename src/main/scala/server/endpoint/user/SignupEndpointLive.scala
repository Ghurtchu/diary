package server.endpoint.user

import route.handler.*
import route.handler.RequestHandlerDefinitions.SignupHandler
import route.service.SignupServiceLive
import server.endpoint.user.UserEndpointDefinitions.SignupEndpoint
import zhttp.http.*
import zio.*

final case class SignupEndpointLive(signupHandler: SignupHandler) extends SignupEndpoint:

  override lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case request@Method.POST -> !! / "api" / "user" / "signup" => signupHandler handle request 
  }


object SignupEndpointLive:
  
  lazy val layer: URLayer[SignupHandler, SignupEndpoint] =
    ZLayer.fromFunction(SignupEndpointLive.apply)
