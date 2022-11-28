package search

enum SearchCriteria:
  self =>
  
  case Exact
  case NonExact
  
  def fold[A](ifExact: => A)(ifNonExact: => A): A = self match
    case Exact    => ifExact
    case NonExact => ifNonExact

object SearchCriteria:
  
  def exact: SearchCriteria    = Exact
  def nonExact: SearchCriteria = NonExact
