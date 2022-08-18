package route.handler

import route.interface.*
import zio.*
import route.implementation.SignupService
import zhttp.http.Response
import model._
import route.interface.CanCreateRecord
import zio.json.*
import route.interface.CommonRequestHandler
import zhttp.http.Request
import model.LoginPayload

class SignupRoute extends CommonRequestHandler[Request] {

  val signupService: CanSignUp[User] = SignupService()

  def handle(request: Request): Task[Response] = for {
    recordAsJson <- request.bodyAsString
    userPayload  <- ZIO.succeed(recordAsJson.fromJson[User])
    errorOrToken <- userPayload.fold(
      err     => ZIO.fail(new RuntimeException(err)),
      payload => signupService signUp payload
    )
    response     <- errorOrToken.fold(
      err => ZIO.succeed(Response.text(err)),
      suc => ZIO.succeed(Response.text(suc))
    )
  } yield response

}
