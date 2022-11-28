package db

enum DbError(val msg: String):

  case ReasonUnknown(override val msg: String) extends DbError(msg)
  case InvalidId(override val msg: String)     extends DbError(msg)
  case NotFound(override val msg: String)      extends DbError(msg)
