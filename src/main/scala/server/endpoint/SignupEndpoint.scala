package server.endpoint

import route.handler.*
import route.implementation.SignupServiceImpl
import zhttp.http._
import zio._

final case class SignupEndpoint(signupHandler: SignupHandler) {

  lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case request@Method.POST -> !! / "api" / "user" / "signup" => signupHandler handle request 
  }

}

object SignupEndpoint {
  
  lazy val layer: URLayer[SignupHandler, SignupEndpoint] =
    ZLayer.fromFunction(SignupEndpoint.apply _)
  
}
