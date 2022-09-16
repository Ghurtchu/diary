package server.middleware

import auth.JwtDecoderLive
import zhttp.http.{Middleware, Request, Response}

object RequestContextMiddleware:
  
  final lazy val jwtAuthMiddleware: Middleware[RequestContextManager, Nothing, Request, Response, Request, Response] = 
    JwtValidatorMiddlewareLive(JwtDecoderLive()).validate
