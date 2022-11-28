package auth

import domain.Domain.JwtContent

trait JwtDecoder:
  def decode(token: String): Either[JwtDecodingError, JwtContent]
