package auth

import pdi.jwt.JwtAlgorithm
import zio.{RIO, Task}
import db.*
import route.service.LoginServiceLive.layer
import hash.{PasswordHashService, SecureHashService}
import zio.*

import java.time.Instant
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}
import io.circe.*
import jawn.parse as jawnParse
import zio.json.*
import JwtEncoderLive.DAY_IN_SECONDS
import domain.Domain.{JwtContent, LoginPayload, User}
import route.service.ServiceDefinitions.LoginService.JWT

final case class JwtEncoderLive() extends JwtEncoder[User]:
  
  override def encode(user: User): JWT =
    val key   = scala.util.Properties.envOrElse("JWT_PRIVATE_KEY", "default private key")
    val algo  = JwtAlgorithm.HS256
    val claim = JwtClaim(
      expiration = Some(Instant.now.plusSeconds(DAY_IN_SECONDS).getEpochSecond),
      issuedAt   = Some(Instant.now.getEpochSecond),
      content    = JwtContent(user.id.get, user.name, user.email).toJsonPretty
    )

    JWT(JwtCirce.encode(claim, key, algo))


object JwtEncoderLive:

  val DAY_IN_SECONDS: Long = 60 * 60 * 24
  
  lazy val layer: ULayer[JwtEncoder[User]] = ZLayer.succeed(JwtEncoderLive())
