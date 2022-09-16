package route.implementation

import model.{JwtContent, LoginPayload}
import zio.{RIO, Task}
import db.*
import route.implementation.LoginServiceLive.layer
import hash.{PasswordHashService, SecureHashService}
import zio.*

import java.time.Instant
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}
import io.circe.*
import jawn.parse as jawnParse
import zio.json.*
import model.JwtContent.*
import auth.JwtEncoder
import model.*
import route.interface.LoginService
import route.interface.LoginService.*

final case class LoginServiceLive(
                                   userRepository: UserRepository,
                                   passwordHashService: PasswordHashService,
                                   jwtEncoder: JwtEncoder[User]
                      ) extends LoginService:
  final override def login(loginPayload: LoginPayload): Task[Either[LoginError, JWT]] =
    userRepository
      .getUserByEmail(loginPayload.email)
      .map(_.fold(Left(LoginError("User does not exist")))(user => getJwtOrAuthFailure(loginPayload.password, user)))
  
  private def getJwtOrAuthFailure(loginPassword: String, user: User): Either[LoginError, JWT] =
    val passwordMatch = passwordHashService.validate(loginPassword, user.password)
    if passwordMatch then Right(jwtEncoder.encode(user)) else Left(LoginError("Auth failed"))
  

object LoginServiceLive:
    
  lazy val layer: URLayer[UserRepository & PasswordHashService & JwtEncoder[User], LoginService] = 
    ZLayer.fromFunction(LoginServiceLive.apply)
