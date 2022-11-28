package route.service

import auth.JwtEncoder
import db.*
import db.user.UserRepository
import hash.{PasswordHashService, SecureHashService}
import io.circe.*
import io.circe.jawn.parse as jawnParse
import domain.*
import domain.Domain.{LoginPayload, User}
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}
import route.service.LoginServiceLive.layer
import zio.*
import zio.json.*
import zio.ZLayer

import java.time.Instant
import route.service.ServiceDefinitions.LoginService
import route.service.ServiceDefinitions.LoginService.{JWT, LoginError}

final case class LoginServiceLive(
                                   private val userRepository: UserRepository,
                                   private val passwordHashService: PasswordHashService,
                                   private val jwtEncoder: JwtEncoder[User]
                      ) extends LoginService:
  override def login(loginPayload: LoginPayload): Task[Either[LoginError, JWT]] =
    userRepository
      .getUserByEmail(loginPayload.email)
      .map(_.fold(Left(LoginError("User does not exist")))(user => getJwtOrAuthFailure(loginPayload.password, user)))
  
  private def getJwtOrAuthFailure(loginPassword: String, user: User): Either[LoginError, JWT] =
    val passwordMatch = passwordHashService.validate(loginPassword, user.password)
    if passwordMatch then Right(jwtEncoder.encode(user)) else Left(LoginError("Auth failed"))
  

object LoginServiceLive:
    
  lazy val layer: URLayer[UserRepository & PasswordHashService & JwtEncoder[User], LoginService] = 
    ZLayer.fromFunction(LoginServiceLive.apply)
