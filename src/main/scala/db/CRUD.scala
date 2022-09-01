package db

import zio.{Task, UIO}

import CRUD._

trait CRUD[A] {
  
  def getById(id: Long): Task[Option[A]]

  def getAll: Task[List[A]]

  def update(id: Long, a: A): Task[UpdateStatus]

  def delete(id: Long): Task[DeletionStatus]

  def add(a: A): Task[CreationStatus]
  
}

object CRUD {

  type CreationStatus = Either[String, String]
  type UpdateStatus   = Either[String, String]
  type DeletionStatus = Either[String, String]

}

