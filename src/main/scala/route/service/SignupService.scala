package route.service

import db.*
import model.*
import route.interface.{CanCreateRecord, CanSignUp}
import zio.*
import zio.json.*

class SignupService extends CanSignUp[User, JWT] {

  val userRepository: UserCRUD = UserRepository()
  
  override def signUp(userPayload: User): Task[Either[String, JWT]] = for {
    userExists  <- userRepository userExists userPayload.email
    response    <-  {
      if userExists then ZIO.succeed(Left("error"))
      else {
        for {
          _         <- userRepository add userPayload
          userAdded <- userRepository.userExists(userPayload.email)
          errorOrJwt  <- {
            if userAdded then ZIO.succeed(Right(JWT("Token")))
            else ZIO.succeed(Left("error"))
          }
        } yield errorOrJwt
      }
    }
  } yield response

}
