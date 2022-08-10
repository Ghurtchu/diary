package model

import zio.json._

import java.util.Date

case class Note(id: Int = null.asInstanceOf[Int], title: String, body: String, createdAt: String, owner: User)

object Note:
  implicit val decoder: JsonDecoder[Note] = DeriveJsonDecoder.gen[Note]
  implicit val encoder: JsonEncoder[Note] = DeriveJsonEncoder.gen[Note]

case class User(id: Int, firstName: String, lastName: String)

object User:
  implicit val decoder: JsonDecoder[User] = DeriveJsonDecoder.gen[User]
  implicit val encoder: JsonEncoder[User] = DeriveJsonEncoder.gen[User]

