package util.sort

import zio.Task

trait SortService[A] {
  def sort(sortOrder: SortOrder, userId: Int): Task[List[A]]
}

sealed trait SortOrder { self =>
  def fold[A](ifAscending: => A)(ifDescending: => A): A = self match
    case SortOrder.Ascending  => ifAscending
    case SortOrder.Descending => ifDescending
}

object SortOrder {
  case object Ascending extends SortOrder
  case object Descending extends SortOrder
}

