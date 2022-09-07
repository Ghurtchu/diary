package server.middleware

import zhttp.http.{Middleware, Request, Response}

trait JwtValidatorMiddleware:
  
  def validate: Middleware[RequestContextManager, Nothing, Request, Response, Request, Response]
