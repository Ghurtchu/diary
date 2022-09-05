package db.mongo

import zio.*
import org.mongodb.scala.*

trait DatabaseInitializer:
  def initialize(DBConfig: DBConfig): RIO[DataSource, Unit]


final case class MongoDatabaseInitializer(dataSource: DataSource) extends DatabaseInitializer:

  override def initialize(dbConfig: DBConfig): UIO[Unit] =
    (for
      _      <- Console.printLine(s"Attempting to establish the connection with MongoDB on port: ${dbConfig.port} with db ${dbConfig.name}")
      client <- ZIO.attempt(MongoClient(dbConfig.port))
      db     <- ZIO.attempt(client.getDatabase(dbConfig.name)) <* Console.printLine("Established connection with database successfully")
      _      <- dataSource.setCtx(DatabaseContext(db))
    yield ()).orDie


object MongoDatabaseInitializer:

  lazy val layer: URLayer[DataSource, DatabaseInitializer] = ZLayer.fromFunction(MongoDatabaseInitializer.apply)