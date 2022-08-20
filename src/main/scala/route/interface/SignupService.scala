package route.interface

import model.{JWT, User}
import zio.Task
import zio.*

trait SignupService {
  def signUp(user: User): Task[Either[String, String]]
}

object SignupService {
  def signUp(user: User): ZIO[SignupService, Throwable, Either[String, String]] =
    ZIO.serviceWithZIO[SignupService](_.signUp(user))
}
