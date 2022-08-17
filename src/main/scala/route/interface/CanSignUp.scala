package route.interface

import model.JWT
import zio.Task

trait CanSignUp[A, B] {
  def signUp(authPayload: A): Task[Either[String, B]]
}
