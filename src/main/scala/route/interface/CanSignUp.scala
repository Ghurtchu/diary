package route.interface

import model.JWT
import zio.Task

trait CanSignUp[A] {
  def signUp(authPayload: A): Task[Either[String, String]]
}
