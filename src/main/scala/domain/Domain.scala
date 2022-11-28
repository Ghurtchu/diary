package domain

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

object Domain {
  
  final case class JwtContent(userId: Long, name: String, email: String)

  object JwtContent:
    given decoder: JsonDecoder[JwtContent] = DeriveJsonDecoder.gen[JwtContent]
    given encoder: JsonEncoder[JwtContent] = DeriveJsonEncoder.gen[JwtContent]

  final case class LoginPayload(email: String, password: String)

  object LoginPayload:
    given decoder: JsonDecoder[LoginPayload] = DeriveJsonDecoder.gen[LoginPayload]
    given encoder: JsonEncoder[LoginPayload] = DeriveJsonEncoder.gen[LoginPayload]

  final case class Note(id: Option[Long], title: String, body: String, createdAt: String, userId: Option[Long])

  object Note:
    given decoder: JsonDecoder[Note] = DeriveJsonDecoder.gen[Note]
    given encoder: JsonEncoder[Note] = DeriveJsonEncoder.gen[Note]

    def apply(id: Long, title: String, body: String, createdAt: String, userId: Long): Note = new Note(id.some, title, body, createdAt, userId.some)
    def apply(title: String, body: String, createdAt: String, userId: Long): Note = new Note(None, title, body, createdAt, userId.some)
    def apply(id: Long, title: String, body: String, createdAt: String): Note = new Note(id.some, title, body, createdAt, None)

  final case class User(id: Option[Long], name: String, email: String, password: String)

  object User:
    given decoder: JsonDecoder[User] = DeriveJsonDecoder.gen[User]
    given encoder: JsonEncoder[User] = DeriveJsonEncoder.gen[User]

    def apply(id: Long, name: String, email: String, password: String): User = new User(id.some, name, email, password)
    def apply(name: String, email: String, password: String): User = new User(None, name, email, password)
    
  
}
