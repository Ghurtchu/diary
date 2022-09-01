package db.user

import db.CRUD.{CreationStatus, DeletionStatus, UpdateStatus}
import db.InMemoryDB
import db.mongo.MongoDatabaseProvider
import model.User
import db.user.UserCRUD
import org.mongodb.scala.{Document, MongoDatabase}
import zio.*
import zio.json.*
import org.mongodb.scala.*
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Sorts._

final case class UserRepository() extends UserCRUD {

  val mongo: UIO[MongoDatabase] = MongoDatabaseProvider.get

  override def getById(id: Long): Task[Option[User]] = for {
    db          <- mongo
    queryResult <- ZIO.fromFuture { implicit ec =>
      db.getCollection("user")
        .find(equal("id", id))
        .first()
        .toFuture()
    }
    maybeUser   <- ZIO.succeed {
      Option(queryResult).fold(None) { doc =>
        Some(
          User(
            id       = id,
            name     = doc("name").asString.getValue,
            email    = doc("email").asString.getValue,
            password = doc("password").asString.getValue
          )
        )
      }
    }
  } yield maybeUser

  override def getAll: UIO[List[User]] = ZIO.succeed(Nil)

  override def update(id: Long, newUser: User): Task[UpdateStatus] = ZIO.succeed(Left("Unimplemented"))

  override def delete(id: Long): Task[DeletionStatus] = ZIO.succeed(Left("Unimplemented"))

  override def add(user: User): Task[CreationStatus] = for {
    db             <- mongo
    resultSequence <- ZIO.fromFuture { implicit ec =>
      db.getCollection("user")
        .insertOne(Document(user.toJson))
        .toFuture()
    }
    creationStatus <- ZIO.succeed(if resultSequence.wasAcknowledged then Right("User has been added") else Left("User was not added"))
  } yield creationStatus

  override def userExists(email: String): Task[Boolean] = for {
    db             <- mongo
    resultSequence <- ZIO.fromFuture { implicit ec =>
      db.getCollection("user")
        .find(equal("email", email))
        .toFuture()
    }
    userExists    <- ZIO.succeed(resultSequence.nonEmpty)
  } yield userExists

  override def getUserByEmail(email: String): Task[Option[User]] = for {
    db <- mongo
    queryResult <- ZIO.fromFuture { implicit ec =>
      db.getCollection("user")
        .find(equal("email", email))
        .first()
        .toFuture()
    }
    maybeUser <- ZIO.succeed {
      Option(queryResult).fold(None) { doc =>
        Some(
          User(
            id       = doc("id").asInt64.getValue,
            name     = email,
            email    = doc("email").asString.getValue,
            password = doc("password").asString.getValue
          )
        )
      }
    }
  } yield maybeUser
  
}

object UserRepository {
  lazy val layer: ULayer[UserCRUD] = ZLayer.fromFunction(UserRepository.apply _)
}
