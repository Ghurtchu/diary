package model

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

final case class JwtContent(id: Long, name: String, email: String)

object JwtContent {
  given decoder: JsonDecoder[JwtContent] = DeriveJsonDecoder.gen[JwtContent]
  given encoder: JsonEncoder[JwtContent] = DeriveJsonEncoder.gen[JwtContent]
}
