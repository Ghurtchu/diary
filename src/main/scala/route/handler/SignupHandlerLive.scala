package route.handler

import route.interface.*
import zio.*
import route.implementation.SignupServiceLive
import zhttp.http.Response
import model._
import route.interface.CreateNoteService
import zio.json.*
import zhttp.http.Request
import model.LoginPayload

trait SignupHandler:

  def handle(request: Request): Task[Response]


final case class SignupHandlerLive(signupService: SignupService) extends SignupHandler:

  final override def handle(request: Request): Task[Response] = 
    for
      userEither <- request.bodyAsString.map(_.fromJson[User])
      response   <- userEither.fold(_ => ZIO.succeed(Response.text("invalid Json")), mapSignupServiceResultToResponse)
    yield response
  
  
  private def mapSignupServiceResultToResponse(user: User): Task[Response] =
    for
      errorOrToken <- signupService signUp user
      response <- ZIO.succeed(errorOrToken.fold(_ => Response text "Sign up failed", token => Response text token))
    yield response
  

object SignupHandlerLive:

  lazy val layer: URLayer[SignupService, SignupHandler] =
    ZLayer.fromFunction(SignupHandlerLive.apply)

