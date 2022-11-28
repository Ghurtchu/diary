package route.handler

import zhttp.http.*
import zio.*
import zio.json.*
import db.*
import hash.{PasswordHashService, SecureHashService}

import java.time.Instant
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

import java.time.Instant
import io.circe.*
import jawn.parse as jawnParse
import RequestHandlerDefinitions.LoginHandler
import domain.Domain.LoginPayload
import route.service.ServiceDefinitions.LoginService

final case class LoginHandlerLive(loginService: LoginService) extends LoginHandler:
  
  override def handle(request: Request): Task[Response] = 
    for
      loginPayloadEither <- request.bodyAsString.map(_.fromJson[LoginPayload])
      response           <- loginPayloadEither.fold(_ => ZIO.succeed(Response.text("Invalid Json").setStatus(Status.BadRequest)), processLoginPayload)
    yield response
    
  private def processLoginPayload(loginPayload: LoginPayload): Task[Response] =
    loginService
      .login(loginPayload)
      .map(_.fold(
        err => Response.text(err.value).setStatus(Status.Unauthorized),
        jwt => Response.text(s"""{"token": ${jwt.value}""").setStatus(Status.Ok))
      )

object LoginHandlerLive:
  
  lazy val layer: URLayer[LoginService, LoginHandler] = 
    ZLayer.fromFunction(LoginHandlerLive.apply)
