package server.endpoint

import route.handler.LoginHandler
import route.implementation.LoginServiceImpl
import server.NotesServer
import zhttp.http.*
import zio.*

final case class LoginEndpoint(loginHandler: LoginHandler) {

  lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case request@Method.POST -> !! / "api" / "user" / "login" => loginHandler.handle(request)
  }

}

object LoginEndpoint {
  
  lazy val layer: URLayer[LoginHandler, LoginEndpoint] =
    ZLayer.fromFunction(LoginEndpoint.apply _)
  
}
