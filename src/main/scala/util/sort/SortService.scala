package util.sort

import zio.Task

trait SortService[A] {
  def sort(sortOrder: SortOrder): Task[List[A]]
}

sealed trait SortOrder

object SortOrder {
  case object Ascending extends SortOrder
  case object Descending extends SortOrder
}

