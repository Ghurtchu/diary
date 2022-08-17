package db

import model.User
import zio._

trait UserCRUD extends CRUD[User] {
  def userExists(email: String): Task[Boolean]
}