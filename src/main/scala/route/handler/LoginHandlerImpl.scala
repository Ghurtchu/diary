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
import route.interface._

trait LoginHandler {
  def handle(request: Request): Task[Response]
}

final case class LoginHandlerImpl(loginService: LoginService) extends LoginHandler {
  
  final override def handle(request: Request): Task[Response] = for {
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

object LoginHandlerImpl {
  
  lazy val layer: URLayer[LoginService, LoginHandlerImpl] = 
    ZLayer.fromFunction(LoginHandlerImpl.apply _)
  
}
