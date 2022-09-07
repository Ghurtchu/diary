package route.interface

import zio.*
import model.LoginPayload
import route.interface.LoginService._

trait LoginService:
  
  def login(loginPayload: LoginPayload): Task[Either[LoginError, JWT]]


object LoginService:
  
  case class JWT(value: String)
  case class LoginError(value: String)