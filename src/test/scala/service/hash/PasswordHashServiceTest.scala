package service.hash

import db.InMemoryDB
import io.github.nremond.legacy.SecureHash
import munit.FunSuite

class PasswordHashServiceTest extends FunSuite {

  val service: CanHashPassword = SecureHashService

  test("hash") {
    (1 to 25).foreach { _ =>
      val randomPass = scala.util.Random.nextString(10)
      val hash = service hash randomPass
      assert(service.validate(randomPass, hash))
    }
  }
}
