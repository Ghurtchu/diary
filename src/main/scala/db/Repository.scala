package db

import db.DbError.InvalidId
import db.Repository.*
import zio.*

trait Repository[A]:
  
  def add(newRecord: A): Task[DbOperation]
  
  def getById(id: Long): Task[Option[A]]
  
  def update(id: Long, newRecord: A): Task[DbOperation]
  
  def delete(id: Long): Task[DbOperation]

object Repository:
    
  type DbOperation = Either[DbError, DbSuccess]

