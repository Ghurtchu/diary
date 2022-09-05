package db.mongo

import org.mongodb.scala.MongoDatabase

final case class DatabaseContext(mongoDatabase: Option[MongoDatabase])

object DatabaseContext:
  
  def initial: DatabaseContext = new DatabaseContext(None)
  
  def apply(mongoDatabase: MongoDatabase): DatabaseContext = new DatabaseContext(Some(mongoDatabase))