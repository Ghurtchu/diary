package route.handler

import route.interface.*
import zio.*
import route.implementation.SignupServiceLive
import zhttp.http.Response
import model._
import route.interface.RecordCreator
import zio.json.*
import zhttp.http.Request
import model.LoginPayload

trait SignupHandler {
  def handle(request: Request): Task[Response]
}

final case class SignupHandlerLive(signupService: SignupService) extends SignupHandler {

  final override def handle(request: Request): Task[Response] = for {
    recordAsJson  <- request.bodyAsString
    userPayload   <- ZIO.succeed(recordAsJson.fromJson[User])
    errorOrToken  <- userPayload.fold(
      err     => ZIO.fail(new RuntimeException(err)),
      signupService.signUp
    )
    response     <- errorOrToken.fold(
      err => ZIO.succeed(Response.text(err)),
      suc => ZIO.succeed(Response.text(suc))
    )
  } yield response

}

object SignupHandlerLive {
  
  lazy val layer: URLayer[SignupService, SignupHandler] =
    ZLayer.fromFunction(SignupHandlerLive.apply _)
  
}
