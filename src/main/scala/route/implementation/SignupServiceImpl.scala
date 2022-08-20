package route.implementation

import db.*
import model.*
import route.interface.{CanCreateRecord, SignupService}
import util.hash.{PasswordHashService, SecureHashService}
import zio.*
import zio.json.*

case class SignupServiceImpl private (
                         private val passwordHashService: PasswordHashService,
                         private val userRepository: UserRepository
                       ) extends SignupService {

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

object SignupServiceImpl {

  def spawn(passwordHashService: PasswordHashService, userRepository: UserRepository): SignupServiceImpl =
    new SignupServiceImpl(passwordHashService, userRepository)

  def layer: ZLayer[Any, Nothing, SignupServiceImpl] =
    (SecureHashService.layer ++ UserRepository.layer) >>> ZLayer.fromFunction(SignupServiceImpl.spawn)

}
