package db.mongo

import org.mongodb.scala.MongoDatabase
import zio.{UIO, ZIO}

trait DataSource:
  
  def setCtx(ctx: DatabaseContext): UIO[Unit]
  
  def getCtx: UIO[DatabaseContext]
