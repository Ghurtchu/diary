package util.search

import route.handler.SearchCriteria
import zio.Task
import route.handler.SearchCriteria

trait SearchService[A]:
  def searchByTitle(title: String, criteria: SearchCriteria, userId: Long): Task[Either[String, List[A]]]

