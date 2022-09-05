package model

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

final case class User (id: Option[Long], name: String, email: String, password: String)

object User:

  given decoder: JsonDecoder[User] = DeriveJsonDecoder.gen[User]
  given encoder: JsonEncoder[User] = DeriveJsonEncoder.gen[User]

  def apply(id: Long, name: String, email: String, password: String): User = new User(id.some, name, email, password)
  def apply(name: String, email: String, password: String): User = new User(None, name, email, password)