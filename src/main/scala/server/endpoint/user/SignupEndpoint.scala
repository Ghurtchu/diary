package server.endpoint.user

import route.handler.*
import route.implementation.SignupServiceLive
import zhttp.http.*
import zio.*

trait SignupEndpoint:
  def route: HttpApp[Any, Throwable]


final case class SignupEndpointLive(signupHandler: SignupHandler) extends SignupEndpoint:

  override lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case request@Method.POST -> !! / "api" / "user" / "signup" => signupHandler handle request 
  }

object SignupEndpointLive:
  
  lazy val layer: URLayer[SignupHandler, SignupEndpoint] =
    ZLayer.fromFunction(SignupEndpointLive.apply)
