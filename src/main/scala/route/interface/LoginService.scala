package route.interface

import zio._
import model.LoginPayload

case class JWT(value: String)
case class LoginError(value: String)

trait LoginService:
  def login(loginPayload: LoginPayload): RIO[Any, Either[LoginError, JWT]]
