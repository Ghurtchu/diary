package service.hash

trait CanHashPassword {
  def hash(hashable: String): String
  def validate(password: String, hashedPassword: String): Boolean
}
