package db.mongo

import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.configuration.CodecRegistries._
import org.mongodb.scala.MongoDatabase
import zio.*

object MongoDatabaseProvider {

  lazy final val get: UIO[MongoDatabase] = (for {
    port          <- System.envOrElse("MONGO_PORT", "mongodb://localhost:27018")
    name          <- System.envOrElse("MONGO_DB_NAME", "notesdb")
    dbConfig      <- ZIO.succeed(DBConfig(port, name))
    mongoDatabase <- MongoDatabaseBuilder(dbConfig).build
  } yield mongoDatabase).orDie


}
