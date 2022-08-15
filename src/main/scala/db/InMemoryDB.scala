package db

import model.{Note, User}

import scala.collection.mutable.ListBuffer

object InMemoryDB:

  lazy val users: Map[Int, User] = Map(
    1 -> User(1, "Nika", "Ghurtchumelia"),
    2 -> User(2, "Ozzy", "Osbourne"),
    3 -> User(3, "Tony", "Iommi"),
    4 -> User(4, "Geezer", "Butler")
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

