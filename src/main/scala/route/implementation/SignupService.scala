package route.implementation

import db.*
import model.*
import route.interface.{CanCreateRecord, CanSignUp}
import service.hash.{CanHashPassword, SecureHashService}
import zio.*
import zio.json.*

class SignupService extends CanSignUp[User] {

  val passwordHashService: CanHashPassword = SecureHashService
  val userRepository: UserCRUD             = UserRepository()
  
  override def signUp(user: User): Task[Either[String, String]] = for {
    userExists  <- userRepository userExists user.email
    response    <-  {
      if userExists then ZIO.succeed(Left("User already exists"))
      else {
        for {
          userWithHashedPass <- ZIO.succeed(user.copy(password = passwordHashService.hash(user.password)))
          signupStatus       <- userRepository add userWithHashedPass
        } yield signupStatus
      } 
    }
  } yield response

}
