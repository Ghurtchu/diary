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


case class User (id: Option[Int], name: String, email: String, password: String)

object User {

  given decoder: JsonDecoder[User] = DeriveJsonDecoder.gen[User]
  given encoder: JsonEncoder[User] = DeriveJsonEncoder.gen[User]

  def apply(id: Int, name: String, email: String, password: String): User = new User(id.some, name, email, password)
  def apply(name: String, email: String, password: String): User = new User(None, name, email, password)

}

case class LoginPayload(email: String, password: String)

object LoginPayload {
  given decoder: JsonDecoder[LoginPayload] = DeriveJsonDecoder.gen[LoginPayload]
  given encoder: JsonEncoder[LoginPayload] = DeriveJsonEncoder.gen[LoginPayload]
}

case class JWT(token: String)
case class JWTError(error: String)
