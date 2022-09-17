package db.user

import db.Repository
import model.User
import zio.Task

trait UserRepository extends Repository[User] :

  def userExists(email: String): Task[Boolean]

  def getUserByEmail(email: String): Task[Option[User]]
