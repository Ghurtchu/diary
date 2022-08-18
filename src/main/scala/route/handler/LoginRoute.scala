package route.handler

import route.interface.CommonRequestHandler
import zhttp.http.*
import zio.*
import zio.json.*
import db.*
import model.*
import model.LoginPayload
import service.hash.CanHashPassword
import service.hash.SecureHashService

class LoginRoute extends CommonRequestHandler[Request] {

  val userRepository: UserCRUD = UserRepository()
  val passwordHashService: CanHashPassword = SecureHashService

  final override def handle(request: Request): Task[Response] = for {
    loginPayloadAsString <- request.bodyAsString
    loginPayloadEither   <- ZIO.succeed(loginPayloadAsString.fromJson[LoginPayload])
    userExists           <- loginPayloadEither.fold(
      err          => ZIO.fail(new RuntimeException(err)),
      loginPayload => userRepository.userExists(loginPayload.email)
    )
    response             <- {
      if !userExists then ZIO.succeed(Response.text("Auth failed").setStatus(Status.Unauthorized))
      else {
        for {
          data               <- loginPayloadEither.fold(
            err => ZIO.fail(new RuntimeException(err)),
            loginPayload => {
              for {
                maybeUser     <- userRepository.getUserByEmail(loginPayload.email)
                passwordCheck <- maybeUser.fold(ZIO.fail(new RuntimeException("xd")))(user => ZIO.succeed(passwordHashService.validate(loginPayload.password, user.password)))
                maybeJwtToken <- ZIO.succeed {
                  if passwordCheck then ??? else ???
                }
              } yield ???
            }
          )
          a <- ZIO.succeed(Response.text("yes"))
        } yield a
      }
    }
  } yield response

}
