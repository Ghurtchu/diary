package db

enum DbSuccess(val msg: String):

  case Created(override val msg: String) extends DbSuccess(msg)
  case Updated(override val msg: String) extends DbSuccess(msg)
  case Deleted(override val msg: String) extends DbSuccess(msg)