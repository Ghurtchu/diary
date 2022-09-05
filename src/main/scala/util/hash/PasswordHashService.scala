package util.hash

import zio._

trait PasswordHashService:

  def hash(hashable: String): String

  def validate(password: String, hashedPassword: String): Boolean
