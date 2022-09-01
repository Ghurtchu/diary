package route.implementation

import model.{JwtContent, LoginPayload}
import route.interface.{JWT, LoginError, LoginService}
import zio.{RIO, Task}
import db.*
import db.user.UserCRUD
import route.implementation.LoginServiceLive.layer
import util.hash.{PasswordHashService, SecureHashService}
import zio.*

import java.time.Instant
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}
import io.circe.*
import jawn.parse as jawnParse
import zio.json.*
import model.JwtContent.*
import util.auth.JwtEncoder
import model.*

final case class LoginServiceLive(
                      userRepository: UserCRUD,
                      passwordHashService: PasswordHashService,
                      jwtEncoder: JwtEncoder[User]
                      ) extends LoginService {
  final override def login(loginPayload: LoginPayload): RIO[Any, Either[LoginError, JWT]] = for {
    maybeUser            <- userRepository.getUserByEmail(loginPayload.email)
    passwordMatch        <- maybeUser.fold(ZIO.succeed(false))(user => ZIO.succeed(passwordHashService.validate(loginPayload.password, user.password)))
    jwtTokenOrLoginError <- if passwordMatch then {
        ZIO.succeed {
          maybeUser.fold(Left(LoginError("User not found"))) { user =>
            Right(jwtEncoder.encode(user))
          }
        }
      } else ZIO.succeed(Left(LoginError("Auth failed")))
  } yield jwtTokenOrLoginError
}

object LoginServiceLive {
    
  lazy val layer: URLayer[UserCRUD & PasswordHashService & JwtEncoder[User], LoginService] = 
    ZLayer.fromFunction(LoginServiceLive.apply _)
    
}
