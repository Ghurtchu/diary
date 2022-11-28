package route.service

import db.*
import db.user.UserRepository
import hash.{PasswordHashService, SecureHashService}
import domain.*
import zio.*
import zio.json.*
import ServiceDefinitions.SignupService
import domain.Domain.User

final case class SignupServiceLive(
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
            signupStatus       <- userRepository.add(userWithId).map(_.toDBResultMessage)
          yield signupStatus
    yield response

object SignupServiceLive:

  lazy val layer: URLayer[PasswordHashService & UserRepository, SignupService] =
    ZLayer.fromFunction(SignupServiceLive.apply)

