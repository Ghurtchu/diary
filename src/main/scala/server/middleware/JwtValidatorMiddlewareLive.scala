package server.middleware

import auth.{JwtDecoder, JwtDecoderLive}
import zhttp.http.{Http, Middleware, Request, Response}
import zio.ZIO

final case class JwtValidatorMiddlewareLive(jwtDecoder: JwtDecoder) extends JwtValidatorMiddleware:

  override lazy val validate: Middleware[RequestContextManager, Nothing, Request, Response, Request, Response] = new Middleware:
    override def apply[R1 <: RequestContextManager, E1 >: Nothing](http: Http[R1, E1, Request, Response]): Http[R1, E1, Request, Response] =
      http.contramapZIO { request =>
        for
          ctxManager <- ZIO.service[RequestContextManager]
          ctx        <- ctxManager.getCtx
          jwtContent = jwtDecoder.decode(request.bearerToken.fold("")(identity))
          _          <- jwtContent.fold(
            _       => ctxManager.setCtx(ctx.copy(jwtContent = None)),
            content => ctxManager.setCtx(ctx.copy(jwtContent = Some(content)))
          )
        yield request
      }

