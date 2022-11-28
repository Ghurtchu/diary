package auth

import model.JwtContent

trait JwtDecoder:
  def decode(token: String): Either[JwtDecodingError, JwtContent]
