package db

sealed trait DbResponse:
  def msg: String


object DbResponse:
  final case class NotFound(msg: String) extends DbResponse
