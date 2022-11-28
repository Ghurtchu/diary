package db.user

import db.*
import db.DbError.InvalidId
import db.Repository.*
import db.mongo.{DataSource, DatabaseContext}
import domain.Domain.User
import org.mongodb.scala.*
import org.mongodb.scala.model.Filters.*
import org.mongodb.scala.model.Sorts.*
import zio.*
import zio.json.*

final case class UserRepositoryLive(dataSource: DataSource) extends UserRepository:

  private val mongo: UIO[MongoDatabase] = dataSource.getCtx.map(_.mongoDatabase.get)

  override def getById(id: Long): Task[Option[User]] = 
    for
      db          <- mongo
      maybeDoc    <- ZIO.fromFuture { implicit ec =>
        db.getCollection("user")
          .find(equal("id", id))
          .first()
          .toFuture()
      }
      maybeUser   <- ZIO.attempt(parseDocumentToUser(maybeDoc))
    yield maybeUser

  override def update(id: Long, newUser: User): Task[DBResult] =
    for
      db           <- mongo
      updateResult <- ZIO.fromFuture { implicit ec =>
        db.getCollection("user")
          .replaceOne(equal("id", id), Document(newUser.toJson))
          .toFuture()
      }
      updateStatus <- updateResult.fold(updateResult.getModifiedCount == 1, DbSuccess.Updated(s"User with id '$id' has been updated"), DbError.InvalidId(s"User with id '$id' has not been updated"))
    yield updateStatus

  override def delete(id: Long): Task[DBResult] =
    for
      db            <- mongo
      queryResult   <- ZIO.fromFuture { implicit ec =>
        db.getCollection("user")
          .deleteOne(equal("id", id))
          .toFuture()
      }
      deletionStatus <- queryResult.fold(queryResult.getDeletedCount == 1, DbSuccess.Deleted(s"User with id $id hsa been deleted"), DbError.InvalidId(s"Could not delete User. User with id: $id does not exist"))
    yield deletionStatus

  override def add(user: User): Task[DBResult] =
    for
      db              <- mongo
      insertionResult <- ZIO.fromFuture { implicit ec =>
        db.getCollection("user")
          .insertOne(Document(user.toJson))
          .toFuture()
      }
      creationStatus  <- insertionResult.fold(insertionResult.wasAcknowledged, DbSuccess.Created("User has been created"), DbError.ReasonUnknown("User has not been created"))
    yield creationStatus

  override def userExists(email: String): Task[Boolean] =
    for
      db        <- mongo
      resultSequence <- ZIO.fromFuture { implicit ec =>
        db.getCollection("user")
          .find(equal("email", email))
          .toFuture()
      }
      userExists    <- ZIO.succeed(resultSequence.nonEmpty)
    yield userExists

  override def getUserByEmail(email: String): Task[Option[User]] =
    for
      db        <- mongo
      document  <- ZIO.fromFuture { implicit ec =>
        db.getCollection("user")
          .find(equal("email", email))
          .first()
          .toFuture()
      }
      maybeUser <- ZIO.succeed(parseDocumentToUser(document))
    yield maybeUser

  private def parseDocumentToUser(doc: Document) =
    Option(doc).fold(None) { doc =>
      Some(
        User(
          id       = doc("id").asInt64.getValue,
          name     = doc("name").asString.getValue,
          email    = doc("email").asString.getValue,
          password = doc("password").asString.getValue
        )
      )
    }


object UserRepositoryLive:

  lazy val layer: URLayer[DataSource, UserRepository] = ZLayer.fromFunction(UserRepositoryLive.apply)
