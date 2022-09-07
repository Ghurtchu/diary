package db

enum DbError(val msg: String):

  case InvalidId(override val msg: String)     extends DbError(msg)
  case NotFound(override val msg: String)      extends DbError(msg)
  case ReasonUnknown(override val msg: String) extends DbError(msg)
