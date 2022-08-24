package route.handler

import zhttp.http.*
import zio.*
import zio.json.*
import db.*
import model.LoginResponse
import model.LoginPayload
import util.hash.{PasswordHashService, SecureHashService}

import java.time.Instant
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

import java.time.Instant
import io.circe.*
import jawn.parse as jawnParse
import route.interface.{CommonRequestHandler, LoginService}

class LoginRoute extends CommonRequestHandler[Request, LoginService]{

  final override def handle(request: Request): RIO[LoginService, Response] = for {
    loginService       <- ZIO.service[LoginService]
    loginPayloadEither <- request.bodyAsString.flatMap(lp => ZIO.succeed(lp.fromJson[LoginPayload]))
    response           <- loginPayloadEither.fold(
      _ => ZIO.succeed(Response.text("wrong JSON format").setStatus(Status.BadRequest)),
      loginPayload => for {
         loginStatus <- loginService.login(loginPayload)
         jwtOrError  <- loginStatus.fold(
           loginError => ZIO.succeed(Response.text(loginError.value).setStatus(Status.Unauthorized)),
           jwt        => ZIO.succeed(Response.text(s"""{"token": ${jwt.value}""").setStatus(Status.Ok))
         )
      } yield jwtOrError
    )
  } yield response

}
