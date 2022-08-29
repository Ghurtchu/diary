package util.auth

import model.User
import pdi.jwt.JwtAlgorithm
import route.interface.JWT
import model.{LoginPayload, JwtContent}
import route.interface.{JWT, LoginError, LoginService}
import zio.{RIO, Task}
import db.*
import route.implementation.LoginServiceLive.layer
import util.hash.{PasswordHashService, SecureHashService}
import zio.*

import java.time.Instant
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}
import io.circe.*
import jawn.parse as jawnParse
import zio.json._
import model.JwtContent._


final case class JwtEncodingError(msg: String)

trait JwtEncoder[A] {
  def encode(a: A): JWT
}

final case class JwtEncoderLive() extends JwtEncoder[User] {
  override def encode(user: User): JWT = {
    val key = scala.util.Properties.envOrElse("JWT_PRIVATE_KEY", "default private key")
    val algo = JwtAlgorithm.HS256
    val Right(claimJson) = jawnParse(JwtContent(user.id.get, user.name, user.email).toJsonPretty)
    val jwt = JwtCirce.encode(claimJson, key, algo)
    
    JWT(jwt)
  }
}

object JwtEncoderLive {
  
  lazy val layer: ULayer[JwtEncoder[User]] = ZLayer.fromFunction(JwtEncoderLive.apply _)
  
}