package route.service

import db.*
import model.*
import route.interface.{CanCreateRecord, CanSignUp}
import zio.*
import zio.json.*

class SignupService extends CanSignUp[AuthPayload, JWT] {

  val userRepository: UserCRUD = UserRepository()
  
  override def signUp(authPayload: AuthPayload): Task[Either[String, JWT]] = for {
    userExists  <- userRepository userExists authPayload.email
    response    <- {
      if userExists then ZIO.succeed(Left("error"))
      else ZIO.succeed(Right(JWT("jwt token")))
    }
  } yield response

}
