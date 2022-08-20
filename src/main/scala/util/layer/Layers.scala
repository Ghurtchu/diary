package util.layer

import db.UserRepository
import route.implementation.SignupServiceImpl
import util.hash.SecureHashService
import zio.ZLayer

object Layers {

  def fullSignupRouteLayer: ZLayer[Any, Throwable, SignupServiceImpl] =
    (SecureHashService.layer ++ UserRepository.layer) >>> SignupServiceImpl.layer
    
}
