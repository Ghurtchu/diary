package db

import zio.{Task, UIO}

import CRUD._

trait CRUD[A] {
  
  def getById(id: Int): Task[Option[A]]

  def getAll: UIO[List[A]]

  def update(id: Int, a: A): Task[UpdateStatus]

  def delete(id: Int): Task[DeletionStatus]

  def add(a: A): Task[CreationStatus]
  
}

object CRUD {

  type CreationStatus = Either[String, String]
  type UpdateStatus   = Either[String, String]
  type DeletionStatus = Either[String, String]

}

