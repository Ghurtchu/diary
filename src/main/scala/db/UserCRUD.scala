package db

import model.User
import zio._

trait UserCRUD extends CRUD[User] {
  def userExists(email: String): Task[Boolean]
  def getUserByEmail(email: String): Task[Option[User]]
}

object UserCRUD {
  def userExists(email: String): ZIO[UserCRUD, Throwable, Boolean] =
    ZIO.serviceWithZIO[UserCRUD](_.userExists(email))
    
  def getUsreByEmail(email: String): ZIO[UserCRUD, Throwable, Option[User]] =
    ZIO.serviceWithZIO[UserCRUD](_.getUserByEmail(email))
}
