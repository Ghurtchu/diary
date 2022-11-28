package server.endpoint.user

import route.handler.RequestHandlerDefinitions.LoginHandler
import route.service.LoginServiceLive
import server.NotesServer
import server.endpoint.user.UserEndpointDefinitions.LoginEndpoint
import zhttp.http.*
import zio.*

final case class LoginEndpointLive(loginHandler: LoginHandler) extends LoginEndpoint:

  override lazy val route: HttpApp[Any, Throwable] = Http.collectZIO[Request] {
    case request@Method.POST -> !! / "api" / "user" / "login" => loginHandler handle request 
  }

object LoginEndpointLive:
  
  lazy val layer: URLayer[LoginHandler, LoginEndpoint] =
    ZLayer.fromFunction(LoginEndpointLive.apply)

