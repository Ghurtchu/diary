package server.endpoint.user

import route.handler.LoginHandler
import route.implementation.LoginServiceLive
import server.NotesServer
import zhttp.http.*
import zio.*

trait LoginEndpoint {
  def route: HttpApp[Any, Throwable]
}

final case class LoginEndpointLive(loginHandler: LoginHandler) extends LoginEndpoint {

  override lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case request@Method.POST -> !! / "api" / "user" / "login" => loginHandler.handle(request)
  }

}

object LoginEndpointLive {
  
  lazy val layer: URLayer[LoginHandler, LoginEndpoint] =
    ZLayer.fromFunction(LoginEndpointLive.apply _)
  
}
