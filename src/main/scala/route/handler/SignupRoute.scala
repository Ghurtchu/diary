package route.handler

import route.interface.*
import zio.*
import route.service.SignupService
import zhttp.http.Response
import model._
import route.interface.CanCreateRecord
import zio.json.*
import zhttp.http.Request
import model.AuthPayload

class SignupRoute {

  val signupService: CanSignUp[AuthPayload, JWT] = SignupService()

  def handle(request: Request): Task[Response] = for {
    recordAsJson <- request.bodyAsString
    authPayload  <- ZIO.succeed(recordAsJson.fromJson[AuthPayload])
    errorOrToken <- authPayload.fold(
      err     => ZIO.fail(new RuntimeException(err)),
      payload => signupService signUp payload
    )
    response     <- errorOrToken.fold(
      err => ZIO.succeed(Response.text(err)),
      jwt => ZIO.succeed(Response.text(jwt.token))
    )
  } yield response

}
