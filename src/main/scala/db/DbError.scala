package db

sealed trait DbError:
  def msg: String

object DbError:
  final case class InvalidId(msg: String)     extends DbError
  final case class NotFound(msg: String)      extends DbError
  final case class ReasonUnknown(msg: String) extends DbError
