package auth

import route.service.ServiceDefinitions.LoginService.JWT

trait JwtEncoder[A]:
  def encode(a: A): JWT
