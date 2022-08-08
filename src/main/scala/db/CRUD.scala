package db

import zio.{Task, UIO}

trait CRUD[A]:
  def getById(id: Int): Task[Option[A]]

  def getAll: UIO[List[A]]

  def update(id: Int, a: A): Task[Boolean]

  def delete(id: Int): Task[Boolean]

  def add(a: A): Task[Boolean]
