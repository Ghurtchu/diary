package db.user

import db.CRUD
import model.User
import zio.Task

trait UserCRUD extends CRUD[User] {
  
  def userExists(email: String): Task[Boolean]

  def getUserByEmail(email: String): Task[Option[User]]
  
}
