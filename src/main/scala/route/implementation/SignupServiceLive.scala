package route.implementation

import db.*
import model.*
import route.interface.{CreateNoteService, SignupService}
import util.hash.{PasswordHashService, SecureHashService}
import zio.*
import zio.json.*

final case class SignupServiceLive private(
                         private val passwordHashService: PasswordHashService,
                         private val userRepository: UserRepository
                       ) extends SignupService:

  override def signUp(user: User): Task[Either[String, String]] = 
    for
      userExists  <- userRepository userExists user.email
      response    <- 
        if userExists then ZIO.succeed(Left("User already exists"))
        else 
          for 
            userWithHashedPass <- ZIO.succeed(user.copy(password = passwordHashService.hash(user.password)))
            userWithId         <- ZIO.succeed(userWithHashedPass.copy(id = Some(scala.util.Random.nextLong(Long.MaxValue))))
            signupStatus       <- userRepository.add(userWithId).map(_.dbOperationMessages)
          yield signupStatus
    yield response

object SignupServiceLive:

  def layer: URLayer[PasswordHashService & UserRepository, SignupService] =
    ZLayer.fromFunction(SignupServiceLive.apply)

