package hash

import zio._

trait PasswordHashService:

  def hash(password: String): String

  def validate(password: String, hashedPassword: String): Boolean
