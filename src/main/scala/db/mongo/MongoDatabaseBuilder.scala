package db.mongo

import zio._
import org.mongodb.scala.*

final case class MongoDatabaseBuilder(dbConfig: DBConfig) {

  lazy final val build: UIO[MongoDatabase] = for {
    client        <- ZIO.succeed(MongoClient(dbConfig.port))
    mongoDatabase <- ZIO.succeed(client.getDatabase(dbConfig.name))
  } yield mongoDatabase

}
