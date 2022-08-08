package service.search

import zio.Task

trait SearchService[A] {
  def searchByTitle(title: String): Task[Option[A]]
}
