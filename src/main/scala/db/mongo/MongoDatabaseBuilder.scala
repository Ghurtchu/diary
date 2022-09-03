package db.mongo

import zio.*
import org.mongodb.scala.*

trait DataSourceBuilder {
  def initialize(DBConfig: DBConfig): RIO[DataSource, Unit]
}

final case class MongoDatabaseBuilder(dataSource: DataSource) extends DataSourceBuilder {

  override def initialize(dbConfig: DBConfig): UIO[Unit] = (for {
    _          <- Console.printLine(s"Attempting to establish the connection with MongoDB on port: ${dbConfig.port} with db ${dbConfig.name}")
    client     <- ZIO.attempt(MongoClient(dbConfig.port))
    db         <- ZIO.attempt(client.getDatabase(dbConfig.name)) <* Console.printLine("Established connection with database successfully")
    _          <- dataSource.setCtx(DatabaseContext(db))
  } yield ()).orDie

}

object MongoDatabaseBuilder {

  def layer: URLayer[DataSource, DataSourceBuilder] = ZLayer.fromFunction(MongoDatabaseBuilder.apply _)

}