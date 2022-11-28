package db

import db.DbError.InvalidId
import db.Repository.*
import zio.*

trait Repository[A]:
  
  def add(newRecord: A): Task[DBResult]
  
  def getById(id: Long): Task[Option[A]]
  
  def update(id: Long, newRecord: A): Task[DBResult]
  
  def delete(id: Long): Task[DBResult]

object Repository:
    
  type DBResult = Either[DbError, DbSuccess]

