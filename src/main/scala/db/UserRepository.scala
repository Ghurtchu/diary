package db
import model.User
import zio._
import CRUD._

class UserRepository extends UserCRUD {

  val inMemoryDB: InMemoryDB.type = InMemoryDB

  override def getById(id: Int): Task[Option[User]] = ZIO.attempt(inMemoryDB.users.get(id))

  override def getAll: UIO[List[User]] = ZIO.succeed(inMemoryDB.users.values.toList)

  override def update(id: Int, newUser: User): Task[UpdateStatus] = ZIO.succeed(Right(""))

  override def delete(id: Int): Task[DeletionStatus] = ZIO.succeed(Right(""))

  override def add(user: User): Task[CreationStatus] = for {
    users          <- ZIO.succeed(inMemoryDB.users)
    newUserId      <- ZIO.succeed(scala.util.Random.nextInt)
    _              <- ZIO.succeed(users.addOne(newUserId, user))
    creationStatus <- ZIO.succeed {
      if users.get(newUserId) == user then Right("User update was successful") else Left("User update was not successful")
    }
  } yield creationStatus

  override def userExists(email: String): Task[Boolean] = ZIO.succeed(inMemoryDB.users.values.map(_.email).toList.contains(email))
  

}
