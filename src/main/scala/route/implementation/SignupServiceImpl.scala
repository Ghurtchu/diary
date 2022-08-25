package route.implementation

import db.*
import model.*
import route.interface.{RecordCreator, SignupService}
import util.hash.{PasswordHashService, SecureHashService}
import zio.*
import zio.json.*

final case class SignupServiceImpl private (
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
          userWithId         <- ZIO.succeed(userWithHashedPass.copy(id = Some(scala.util.Random.nextInt)))
          signupStatus       <- userRepository add userWithId
        } yield signupStatus
      } 
    }
  } yield response

}

object SignupServiceImpl {

  def layer: URLayer[PasswordHashService & UserRepository, SignupServiceImpl] =
    ZLayer.fromFunction(SignupServiceImpl.apply _)

}
