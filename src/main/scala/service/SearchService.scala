package service

import zio._

trait SearchService[A] {
  def searchByTitle(title: String): Task[Option[A]]
}
