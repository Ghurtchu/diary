package route.implementation

import model.{LoginPayload, LoginResponse}
import route.interface.{JWT, LoginError, LoginService}
import zio.{RIO, Task}
import db.*
import route.implementation.LoginServiceImpl.layer
import util.hash.{PasswordHashService, SecureHashService}
import zio.*

import java.time.Instant
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}
import io.circe.*
import jawn.parse as jawnParse
import zio.json._
import model.LoginResponse._

final case class LoginServiceImpl(
                      userRepository: UserCRUD,
                      passwordHashService: PasswordHashService
                      ) extends LoginService {
  final override def login(loginPayload: LoginPayload): RIO[Any, Either[LoginError, JWT]] = for {
    maybeUser <- userRepository.getUserByEmail(loginPayload.email)
    passwordMatch <- maybeUser.fold(ZIO.succeed(false))(user => ZIO.succeed(passwordHashService.validate(loginPayload.password, user.password)))
    jwtTokenOrLoginError <- if passwordMatch then {
        val key = scala.util.Properties.envOrElse("JWT_PRIVATE_KEY", "default private key")
        val algo = JwtAlgorithm.HS256
        val Right(claimJson) = jawnParse(maybeUser.map(user => Some(LoginResponse(user.id.get, user.name, user.email)).get).toJsonPretty)
        val jwt = JwtCirce.encode(claimJson, key, algo)
      
        ZIO.succeed(Right(JWT(jwt)))
      } else ZIO.succeed(Left(LoginError("Auth failed")))
  } yield jwtTokenOrLoginError
}

object LoginServiceImpl {
    
  lazy val layer: URLayer[UserCRUD & PasswordHashService, LoginServiceImpl] = 
    ZLayer.fromFunction(LoginServiceImpl.apply _)
    
}
