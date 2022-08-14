package model

import zio.json._

import java.util.Date

case class Note private(id: Option[Int] = None, title: String, body: String, createdAt: String, owner: User)

object Note {

  given decoder: JsonDecoder[Note] = DeriveJsonDecoder.gen[Note]
  given encoder: JsonEncoder[Note] = DeriveJsonEncoder.gen[Note]

  def apply(id: Int, title: String, body: String, createdAt: String, owner: User): Note = new Note(id.some, title, body, createdAt, owner)
  def apply(title: String, body: String, createdAt: String, owner: User): Note = new Note(None, title, body, createdAt, owner)

}


case class User private(id: Option[Int], firstName: String, lastName: String)

object User {

  given decoder: JsonDecoder[User] = DeriveJsonDecoder.gen[User]
  given encoder: JsonEncoder[User] = DeriveJsonEncoder.gen[User]

  def apply(id: Int, firstName: String, lastName: String): User = new User(id.some, firstName, lastName)
  def apply(firstName: String, lastName: String): User = new User(None, firstName, lastName)

}

