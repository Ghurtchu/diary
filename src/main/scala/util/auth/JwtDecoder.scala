package util.auth

import model.JwtContent
import pdi.jwt.{Jwt, JwtAlgorithm}
import zio.{ULayer, ZLayer}
import zio.json.*

final case class JwtDecodingError(msg: String)

trait JwtDecoder:
  def decode(token: String): Either[JwtDecodingError, JwtContent]


final case class JwtDecoderLive() extends JwtDecoder:
  
  override def decode(token: String): Either[JwtDecodingError, JwtContent] =
    Jwt.decode(token, scala.util.Properties.envOrElse("JWT_PRIVATE_KEY", "default private key"), Seq(JwtAlgorithm.HS256))
      .fold(err => Left(JwtDecodingError(err.getMessage)), claim => {
      val jwtContentEither = claim.content.fromJson[JwtContent]
      jwtContentEither.fold(err => Left(JwtDecodingError(err)), Right(_))
      })

object JwtDecoderLive:
  
  def layer: ULayer[JwtDecoder] = ZLayer.succeed(JwtDecoderLive())

