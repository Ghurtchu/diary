package auth

import route.interface.LoginService.JWT

trait JwtEncoder[A]:
  
  def encode(a: A): JWT
