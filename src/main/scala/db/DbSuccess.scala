package db

trait DbSuccess:
  def msg: String
  
object DbSuccess:
  final case class Created(msg: String) extends DbSuccess  
  final case class Updated(msg: String) extends DbSuccess
  final case class Deleted(msg: String) extends DbSuccess