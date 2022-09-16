package model

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

final case class Note (id: Option[Long], title: String, body: String, createdAt: String, userId: Option[Long])

object Note:

  given decoder: JsonDecoder[Note] = DeriveJsonDecoder.gen[Note]
  given encoder: JsonEncoder[Note] = DeriveJsonEncoder.gen[Note]

  def apply(id: Long, title: String, body: String, createdAt: String, userId: Long): Note = new Note(id.some, title, body, createdAt, userId.some)
  def apply(title: String, body: String, createdAt: String, userId: Long): Note           = new Note(None, title, body, createdAt, userId.some)
  def apply(id: Long, title: String, body: String, createdAt: String): Note               = new Note(id.some, title, body, createdAt, None)
