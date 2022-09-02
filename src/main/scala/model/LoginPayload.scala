package model

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

final case class LoginPayload(email: String, password: String)

object LoginPayload {
  given decoder: JsonDecoder[LoginPayload] = DeriveJsonDecoder.gen[LoginPayload]
  given encoder: JsonEncoder[LoginPayload] = DeriveJsonEncoder.gen[LoginPayload]
}