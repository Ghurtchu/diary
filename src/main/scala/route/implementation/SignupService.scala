package route.implementation

import db.*
import model.*
import route.interface.{CanCreateRecord, CanSignUp}
import zio.*
import zio.json.*

class SignupService extends CanSignUp[User] {

  val userRepository: UserCRUD = UserRepository()
  
  override def signUp(user: User): Task[Either[String, String]] = for {
    userExists  <- userRepository userExists user.email
    response    <-  {
      if userExists then ZIO.succeed(Left("User already exists"))
      else {
        for {
          creationStatus <- userRepository add user
        } yield creationStatus
      } 
    }
  } yield response

}
