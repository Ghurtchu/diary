import org.mongodb.scala.result.{DeleteResult, InsertOneResult, UpdateResult}
import zio.*

package object db {

  extension [A <: DeleteResult | InsertOneResult | UpdateResult] (a: A)
    def fold(wasAcknowledged: => Boolean, onSuccess: => String, onFailure: => String): UIO[Either[String, String]] =
      ZIO.succeed(if wasAcknowledged then Right(onSuccess) else Left(onSuccess))

}
