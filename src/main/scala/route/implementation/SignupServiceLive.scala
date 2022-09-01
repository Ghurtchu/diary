package route.implementation

import db.*
import db.user.UserCRUD
import model.*
import route.interface.{RecordCreator, SignupService}
import util.hash.{PasswordHashService, SecureHashService}
import zio.*
import zio.json.*

final case class SignupServiceLive private(
                         private val passwordHashService: PasswordHashService,
                         private val userRepository: UserCRUD
                       ) extends SignupService {

  override def signUp(user: User): Task[Either[String, String]] = for {
    userExists  <- userRepository userExists user.email
    response    <-  {
      if userExists then ZIO.succeed(Left("User already exists"))
      else {
        for {
          userWithHashedPass <- ZIO.succeed(user.copy(password = passwordHashService.hash(user.password)))
          userWithId         <- ZIO.succeed(userWithHashedPass.copy(id = Some(scala.util.Random.nextLong(Long.MaxValue))))
          signupStatus       <- userRepository add userWithId
        } yield signupStatus
      } 
    }
  } yield response

}

object SignupServiceLive {

  def layer: URLayer[PasswordHashService & UserCRUD, SignupService] =
    ZLayer.fromFunction(SignupServiceLive.apply _)

}
