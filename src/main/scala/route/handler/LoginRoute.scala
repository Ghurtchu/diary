package route.handler

import zhttp.http.*
import zio.*
import zio.json.*
import db.*
import model.UserJWT
import model.LoginPayload
import util.hash.{PasswordHashService, SecureHashService}

import java.time.Instant
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

import java.time.Instant
import io.circe.*
import jawn.parse as jawnParse

class LoginRoute {

  val userRepository: UserCRUD                 = UserRepository()
  val passwordHashService: PasswordHashService = SecureHashService()

  final def handle(request: Request): Task[Response] = for {
    loginPayloadEither <- request.bodyAsString.flatMap(lp => ZIO.succeed(lp.fromJson[LoginPayload]))
    response           <- loginPayloadEither.fold(
      err => ZIO.fail(new RuntimeException(err)),
      loginPayload => for {
        maybeUser     <- userRepository.getUserByEmail(loginPayload.email)
        passwordMatch <- maybeUser.fold(ZIO.succeed(false))(user => ZIO.succeed(passwordHashService.validate(loginPayload.password, user.password)))
        maybeJwtToken <- ZIO.succeed {
          if passwordMatch then {
            val key = scala.util.Properties.envOrElse("JWT_PRIVATE_KEY", "default private key")
            val algo = JwtAlgorithm.HS256
            val Right(claimJson) = jawnParse(maybeUser.map(user => Some(UserJWT(user.id.get, user.name, user.email)).get).toJsonPretty)
            val jwt = JwtCirce.encode(claimJson, key, algo)
            val jsonResponse = s"""{"token": $jwt}"""
            Response.text(jsonResponse)
              .setStatus(Status.Ok)
          }
          else Response.text("Auth Failed")
            .setStatus(Status.Unauthorized)
        }
      } yield maybeJwtToken
    )
  } yield response

}
