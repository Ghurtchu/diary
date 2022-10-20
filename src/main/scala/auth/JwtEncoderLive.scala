package auth

import model.User
import pdi.jwt.JwtAlgorithm
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
import route.interface.LoginService.JWT
import JwtEncoderLive.DAY_IN_SECONDS

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
