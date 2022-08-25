package route.interface

import model._
import zio.Task
import zio.*

trait SignupService {
  def signUp(user: User): Task[Either[String, String]]
}
