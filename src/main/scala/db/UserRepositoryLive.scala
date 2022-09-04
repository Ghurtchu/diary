package db

import db.Repository.{CreationStatus, DeletionStatus, UpdateStatus}
import db.mongo.{DatabaseContext, DataSource}
import db.*
import model.User
import org.mongodb.scala.*
import org.mongodb.scala.model.Filters.*
import org.mongodb.scala.model.Sorts.*
import zio.*
import zio.json.*

final case class UserRepositoryLive(dataSource: DataSource) extends UserRepository {

  override def getById(id: Long): Task[Option[User]] = for {
    db        <- dataSource.get
    maybeDoc <- ZIO.fromFuture { implicit ec =>
      db.getCollection("user")
        .find(equal("id", id))
        .first()
        .toFuture()
    }
    maybeUser   <- ZIO.attempt(parseDocumentToUser(maybeDoc))
  } yield maybeUser

  override def update(id: Long, newUser: User): Task[UpdateStatus] = for {
    db        <- dataSource.get
    queryResult <- ZIO.fromFuture { implicit ec =>
      db.getCollection("user")
        .updateOne(equal("id", id), Document(newUser.toJson))
        .toFuture()
    }
    updateStatus <- queryResult.fold(queryResult.wasAcknowledged, s"User with id '$id' has been updated successfully", s"User with id '$id' has not been updated")
  } yield updateStatus

  override def delete(id: Long): Task[DeletionStatus] = for {
    db        <- dataSource.get
    queryResult <- ZIO.fromFuture { implicit ec =>
      db.getCollection("user")
        .deleteOne(equal("id", id))
        .toFuture()
    }
    deletionStatus <- queryResult.fold(queryResult.wasAcknowledged, s"Note with id '$id' has been deleted", s"Note with id '$id' has not been deleted")
  } yield deletionStatus

  override def add(user: User): Task[CreationStatus] = for {
    db        <- dataSource.get
    queryResult    <- ZIO.fromFuture { implicit ec =>
      db.getCollection("user")
        .insertOne(Document(user.toJson))
        .toFuture()
    }
    creationStatus <- ZIO.succeed {
      if queryResult.wasAcknowledged then Right("User has been added")
      else Left("User was not added")
    }
  } yield creationStatus

  override def userExists(email: String): Task[Boolean] = for {
    db        <- dataSource.get
    resultSequence <- ZIO.fromFuture { implicit ec =>
      db.getCollection("user")
        .find(equal("email", email))
        .toFuture()
    }
    userExists    <- ZIO.succeed(resultSequence.nonEmpty)
  } yield userExists

  override def getUserByEmail(email: String): Task[Option[User]] = for {
    db        <- dataSource.get
    document <- ZIO.fromFuture { implicit ec =>
      db.getCollection("user")
        .find(equal("email", email))
        .first()
        .toFuture()
    }
    maybeUser <- ZIO.succeed(parseDocumentToUser(document))
  } yield maybeUser

  private def parseDocumentToUser(doc: Document) = {
    Option(doc).fold(None) { doc =>
      Some(
        User(
          id = doc("id").asInt64.getValue,
          name = doc("name").asString.getValue,
          email = doc("email").asString.getValue,
          password = doc("password").asString.getValue
        )
      )
    }
  }

}

object UserRepositoryLive {

  lazy val layer: URLayer[DataSource, UserRepository] = ZLayer.fromFunction(UserRepositoryLive.apply)

}
