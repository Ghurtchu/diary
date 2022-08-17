package db

import model.{Note, User}

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map

object InMemoryDB {

  lazy val users: scala.collection.mutable.Map[Int, User] = scala.collection.mutable.Map(
    1 -> User(1, "Nika", "n@at.com", "pass1"),
    2 -> User(2, "Ozzy", "o@at.com", "pass2"),
    3 -> User(3, "Tony", "t@at.com", "pass3"),
    4 -> User(4, "Geezer", "g@at.com", "pass4")
  )

  lazy val notes: ListBuffer[Note] = ListBuffer(
    Note(1, "Z title", "first note body", "2020-01-01", users(1)),
    Note(2, "L title", "second note body", "2020-01-02", users(1)),
    Note(3, "J title", "third note body", "2020-01-03", users(1)),
    Note(4, "F title", "fourth note body", "2021-04-03", users(2)),
    Note("A title", "fifth note body", "2021-05-03", users(2)),
    Note("B title", "sixth note body", "2021-06-03", users(3)),
    Note("C title", "seventh note body", "2021-06-03", users(4)),
    Note(8, "D title", "eighth note body", "2021-06-03", users(4)),
  )

}

