package server.middleware

import io.netty.handler.codec.http.HttpHeaders
import model.JwtContent
import pdi.jwt.{Jwt, JwtAlgorithm}
import zio.*
import zio.json.*
import zhttp.http.*
import zhttp.service.Server
import zhttp.http.middleware.Auth

// A model of something we may want to use in our logic, after processed via Middleware
case class RequestContext(
                           token: Option[String],
                           jwtContent: Option[JwtContent]
                         )

object RequestContext {
  def initial: RequestContext                  = new RequestContext(None, None)
  def fromToken(token: String): RequestContext = new RequestContext(Some(token), None)
}

// A service to manage the current context
trait RequestContextManager {
  def setCtx(ctx: RequestContext): UIO[Unit]
  def getCtx: UIO[RequestContext]
}

final case class RequestContextManagerLive(ref: FiberRef[RequestContext]) extends RequestContextManager {
  override def setCtx(ctx: RequestContext): UIO[Unit] = ref set ctx
  override def getCtx: UIO[RequestContext] = ref.get
}

object RequestContextManagerLive {
  // Provide an "empty" context when we start up the layer
  def layer: ULayer[RequestContextManager] = ZLayer.scoped {
    for {
      ref <- FiberRef.make[RequestContext](RequestContext.initial)
    } yield RequestContextManagerLive(ref)
  }
}

// middleware
object RequestContextMiddleware {

  private final def addJwtToHeader: Middleware[
    RequestContextManager,
    Nothing,
    Request,
    Response,
    Request,
    Response
  ] = new Middleware[
    RequestContextManager,
    Nothing,
    Request,
    Response,
    Request,
    Response
  ] {
    override def apply[R1 <: RequestContextManager, E1 >: Nothing](http: Http[R1, E1, Request, Response]): Http[R1, E1, Request, Response] = {
     http.contramapZIO[R1, E1, Request] { request =>
       for {
         cxtManager <- ZIO.service[RequestContextManager] <* ZIO.succeed(println("addJwtToHeader running..."))
         newCtx     <- ZIO.succeed(RequestContext.fromToken(request.bearerToken.fold("")(identity)))
         _          <- cxtManager.setCtx(newCtx)
         _          <- ZIO.succeed(println("addJwtToHeader done"))
       } yield request
     }
    }
  }

  private final def validateCtx: Middleware[
    RequestContextManager,
    Nothing,
    Request,
    Response,
    Request,
    Response
  ] = new Middleware[
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
          ctxManager <- ZIO.service[RequestContextManager] <* ZIO.succeed(println("validateCtx running..."))
          ctx <- ctxManager.getCtx
          jwtContent = Jwt.decode(
            ctx.token.get,
            scala.util.Properties.envOrElse("JWT_PRIVATE_KEY", "default private key"),
            Seq(JwtAlgorithm.HS256)
          ).toOption.get.content.fromJson[JwtContent]
          _ <- ctxManager.setCtx(ctx.copy(jwtContent = jwtContent.toOption)) <* ZIO.succeed(println("validateCtx done"))
        } yield request
      }
    }
  }
  
  lazy val jwtAuthMiddleware: Middleware[
    RequestContextManager,
    Nothing,
    Request,
    Response,
    Request,
    Response
  ] = validateCtx >>> addJwtToHeader
  
}
