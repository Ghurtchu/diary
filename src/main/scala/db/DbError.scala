package db

sealed trait DbError:

  def msg: String

object DbError:
  final case class InvalidId(override val msg: String) extends DbError

