package db

import db.Repository._
import zio.*

trait Repository[A] {
  
  def add(newRecord: A): Task[CreationStatus]
  
  def getById(id: Long): Task[Option[A]]
  
  def update(id: Long, newRecord: A): Task[UpdateStatus]
  
  def delete(id: Long): Task[DeletionStatus]
  
}

object Repository {
  
  type CreationStatus = Either[String, String]
  type UpdateStatus   = Either[String, String]
  type DeletionStatus = Either[String, String]
  
}
