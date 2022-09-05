package route.handler

import zhttp.http.*
import zio.*
import zio.json.*
import db.*
import model.JwtContent
import model.LoginPayload
import util.hash.{PasswordHashService, SecureHashService}

import java.time.Instant
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

import java.time.Instant
import io.circe.*
import jawn.parse as jawnParse
import route.interface._

trait LoginHandler:
  def handle(request: Request): Task[Response]


final case class LoginHandlerLive(loginService: LoginService) extends LoginHandler:
  
  override def handle(request: Request): Task[Response] = 
    for
      loginPayloadEither <- request.bodyAsString.flatMap(body => ZIO.succeed(body.fromJson[LoginPayload]))
      response           <- loginPayloadEither.fold(
        _ => ZIO.succeed(Response.text("wrong JSON format").setStatus(Status.BadRequest)),
        loginPayload => 
          for
           loginStatus <- loginService.login(loginPayload)
           jwtOrError  <- loginStatus.fold(
             loginError => ZIO.succeed(Response.text(loginError.value).setStatus(Status.Unauthorized)),
             jwt        => ZIO.succeed(Response.text(s"""{"token": ${jwt.value}""").setStatus(Status.Ok)))
          yield jwtOrError)
    yield response

object LoginHandlerLive:
  
  lazy val layer: URLayer[LoginService, LoginHandler] = 
    ZLayer.fromFunction(LoginHandlerLive.apply)
