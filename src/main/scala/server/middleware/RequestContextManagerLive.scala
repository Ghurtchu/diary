package server.middleware

import zio.{FiberRef, UIO, ULayer, ZLayer}

final case class RequestContextManagerLive(ref: FiberRef[RequestContext]) extends RequestContextManager:
  
  override def setCtx(ctx: RequestContext): UIO[Unit] = ref set ctx
  
  override def getCtx: UIO[RequestContext] = ref.get


object RequestContextManagerLive:
  
  def layer: ULayer[RequestContextManager] = ZLayer.scoped {
    for
      ref <- FiberRef.make[RequestContext](RequestContext.initial)
    yield RequestContextManagerLive(ref)
  }
  