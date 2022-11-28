package server.middleware

import io.netty.handler.codec.http.HttpHeaders
import pdi.jwt.{Jwt, JwtAlgorithm}
import auth.{JwtDecoder, JwtDecoderLive}
import domain.Domain.JwtContent
import zio.*
import zio.json.*
import zhttp.http.*
import zhttp.service.Server
import zhttp.http.middleware.Auth

case class RequestContext(jwtContent: Option[JwtContent]):
  
  def getJwtOrFail: Either[Task[Response], JwtContent] = jwtContent.fold(Left(ZIO.succeed(Response.text("Auth failed").setStatus(Status.Unauthorized))))(Right(_))


object RequestContext:

  def initial: RequestContext = new RequestContext(None)


