package route

import db.{DbError, DbSuccess}

import scala.util.Either

package object service:

  extension[A] (value: A)
    def inQuotes: String = s"`$value`"
    
  extension (dbErrorOrSuccess: Either[DbError, DbSuccess])
    def toDBResultMessage: Either[String, String] =
      dbErrorOrSuccess.fold(
        err     => Left(err.msg),
        success => Right(success.msg)
      )

