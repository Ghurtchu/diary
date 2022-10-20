package scala.sort

enum SortOrder:
  self =>

  case Ascending
  case Descending

  def fold[A](ifAscending: => A)(ifDescending: => A): A = self match
    case SortOrder.Ascending  => ifAscending
    case SortOrder.Descending => ifDescending