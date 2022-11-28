import db.Repository.DbOperation
import org.mongodb.scala.result.{DeleteResult, InsertOneResult, UpdateResult}
import zio.*

package object db:

  extension [A <: DeleteResult | InsertOneResult | UpdateResult] (self: A)
    def fold(wasAcknowledged: => Boolean, onSuccess: => DbSuccess, onFailure: => DbError): UIO[DbOperation] =
      ZIO.succeed(if wasAcknowledged then Right(onSuccess) else Left(onFailure))

