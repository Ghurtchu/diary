package server.middleware

import io.netty.handler.codec.http.HttpHeaders
import model.JwtContent
import pdi.jwt.{Jwt, JwtAlgorithm}
import util.auth.{JwtDecoder, JwtDecoderLive}
import zio.*
import zio.json.*
import zhttp.http.*
import zhttp.service.Server
import zhttp.http.middleware.Auth
import JwtValidatorMiddlewareLive.validateJwt

case class RequestContext(
                           jwtContent: Option[JwtContent]
                         ) {
  def getJwtOrFailure: Either[Task[Response], JwtContent] = 
    jwtContent.fold(Left(ZIO.succeed(Response.text("Auth failed").setStatus(Status.Unauthorized))))(Right(_))
}

object RequestContext {
  def initial: RequestContext = new RequestContext(None)
}

trait RequestContextManager {
  def setCtx(ctx: RequestContext): UIO[Unit]
  def getCtx: UIO[RequestContext]
}

final case class RequestContextManagerLive(ref: FiberRef[RequestContext]) extends RequestContextManager {
  override def setCtx(ctx: RequestContext): UIO[Unit] = ref set ctx
  override def getCtx: UIO[RequestContext] = ref.get
}

object RequestContextManagerLive {
  def layer: ULayer[RequestContextManager] = ZLayer.scoped {
    for {
      ref <- FiberRef.make[RequestContext](RequestContext.initial)
    } yield RequestContextManagerLive(ref)
  }
}

trait JwtValidatorMiddleware {
  def validate: Middleware[
    RequestContextManager,
    Nothing,
    Request,
    Response,
    Request,
    Response
  ]
}

final case class JwtValidatorMiddlewareLive(jwtDecoder: JwtDecoder) extends JwtValidatorMiddleware {
  override lazy val validate: Middleware[RequestContextManager, Nothing, Request, Response, Request, Response] = new Middleware[
    RequestContextManager,
    Nothing,
    Request,
    Response,
    Request,
    Response
  ] {
    override def apply[R1 <: RequestContextManager, E1 >: Nothing](http: Http[R1, E1, Request, Response]): Http[R1, E1, Request, Response] = {
      http.contramapZIO { request =>
        for {
          ctxManager <- ZIO.service[RequestContextManager]
          ctx        <- ctxManager.getCtx
          jwtContent = jwtDecoder.decode(request.bearerToken.fold("")(identity))
          _          <- jwtContent.fold(
            err     => ctxManager.setCtx(ctx.copy(jwtContent = None)),
            content => ctxManager.setCtx(ctx.copy(jwtContent = Some(content)))
          )
        } yield request
      }
    }
  }
}

object JwtValidatorMiddlewareLive {

  lazy val validateJwt: Middleware[RequestContextManager, Nothing, Request, Response, Request, Response] = JwtValidatorMiddlewareLive(JwtDecoderLive()).validate

}

object RequestContextMiddleware {

  lazy val jwtAuthMiddleware: Middleware[
    RequestContextManager,
    Nothing,
    Request,
    Response,
    Request,
    Response
  ] = validateJwt

}
