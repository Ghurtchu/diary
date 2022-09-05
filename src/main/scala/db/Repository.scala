package db

import db.DbError.InvalidId
import db.Repository.*
import zio.*

trait Repository[A]:
  
  def add(newRecord: A): Task[CreationStatus]
  
  def getById(id: Long): Task[Option[A]]
  
  def update(id: Long, newRecord: A): Task[Either[DbError, String]]
  
  def delete(id: Long): Task[Either[DbError, String]]

object Repository:
  
  type CreationStatus = Either[String, String]
  type DeletionStatus = Either[String, String]

