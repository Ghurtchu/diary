package auth

import domain.JwtContent

trait JwtDecoder:
  def decode(token: String): Either[JwtDecodingError, JwtContent]
