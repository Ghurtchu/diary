package server.middleware

import zio.UIO

trait RequestContextManager:

  def setCtx(ctx: RequestContext): UIO[Unit]

  def getCtx: UIO[RequestContext]
