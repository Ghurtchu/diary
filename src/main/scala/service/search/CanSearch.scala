package service.search

import zio.Task

trait CanSearch[A] {
  def searchByTitle(title: String): Task[Either[String, A]]
}
