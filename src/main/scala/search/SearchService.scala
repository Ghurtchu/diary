package search

import zio.Task

trait SearchService[A]:
  def searchByTitle(title: String, searchCriteria: SearchCriteria, userId: Long): Task[Either[String, List[A]]]

