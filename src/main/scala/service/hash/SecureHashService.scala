package service.hash

import io.github.nremond.legacy.SecureHash

object SecureHashService extends CanHashPassword {
  
  private final val underlyingImpl: SecureHash = SecureHash()
  
  override def hash(hashable: String): String = underlyingImpl createHash hashable
  
  override def validate(password: String, hashedPassword: String): Boolean = underlyingImpl.validatePassword(password, hashedPassword)

}
