package scala.sort

import zio.Task

trait SortService[A]:
  def sort(sortOrder: SortOrder, userId: Long): Task[List[A]]

