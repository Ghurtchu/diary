package db

sealed trait DbError

object DbError:
  final case class NotFound(msg: String)  extends DbError
  final case class InvalidId(msg: String) extends DbError

