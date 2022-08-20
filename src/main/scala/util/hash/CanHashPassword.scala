package util.hash

import zio._

trait CanHashPassword {
  def hash(hashable: String): String
  def validate(password: String, hashedPassword: String): Boolean
}

object CanHashPassword {
  def hash(hashable: String): ZIO[CanHashPassword, Nothing, String] =
    ZIO.serviceWith[CanHashPassword](_.hash(hashable))
    
  def validate(password: String, hashedPassword: String): ZIO[CanHashPassword, Nothing, Boolean] =
    ZIO.serviceWith[CanHashPassword](_.validate(password, hashedPassword))  
}
