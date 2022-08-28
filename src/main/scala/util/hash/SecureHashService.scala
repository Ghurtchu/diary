package util.hash

import io.github.nremond.legacy.SecureHash
import zio._

final case class SecureHashService() extends PasswordHashService {
  
  private final val underlyingImpl: SecureHash = SecureHash()
  
  override def hash(hashable: String): String = underlyingImpl createHash hashable
  
  override def validate(password: String, hashedPassword: String): Boolean = underlyingImpl.validatePassword(password, hashedPassword)
  
}

object SecureHashService {
  
  lazy val layer: ULayer[PasswordHashService] =
    ZLayer.succeed(SecureHashService())
    
}
