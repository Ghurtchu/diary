package route.handler

import zio.*
import zhttp.http.{Request, Response, Status}
import domain.*
import zio.json.*
import RequestHandlerDefinitions.SignupHandler
import domain.Domain.User
import route.service.ServiceDefinitions.SignupService
import route.service.SignupServiceLive

final case class SignupHandlerLive(signupService: SignupService) extends SignupHandler:

  override def handle(request: Request): Task[Response] =
    for
      userEither <- request.bodyAsString.map(_.fromJson[User])
      response   <- userEither.fold(_ => ZIO.succeed(Response.text("Invalid Json")), mapSignupServiceResultToResponse)
    yield response
  
  
  private def mapSignupServiceResultToResponse(user: User): Task[Response] =
    signupService
      .signUp(user)
      .map(_.fold(
        Response.text(_).setStatus(Status.Conflict),
        token => Response.text(token).setStatus(Status.Created)
      ))
  

object SignupHandlerLive:

  lazy val layer: URLayer[SignupService, SignupHandler] =
    ZLayer.fromFunction(SignupHandlerLive.apply)

