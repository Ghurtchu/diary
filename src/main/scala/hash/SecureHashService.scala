package hash

import io.github.nremond.legacy.SecureHash
import zio._

final case class SecureHashService() extends PasswordHashService:
  
  private final val underlyingImpl: SecureHash = SecureHash()
  
  override def hash(password: String): String = underlyingImpl createHash password
  
  override def validate(password: String, hashedPassword: String): Boolean = underlyingImpl.validatePassword(password, hashedPassword)

object SecureHashService:
  
  lazy val layer: ULayer[PasswordHashService] =
    ZLayer.succeed(SecureHashService())

