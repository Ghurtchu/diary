package db

import io.github.nremond.PBKDF2
import io.github.nremond.legacy.SecureHash
import munit.FunSuite
import java.nio.charset.StandardCharsets

class InMemoryDBTest extends FunSuite {

  test("hash") {
    (1 to 25).foreach { _ =>
      val randomPass = scala.util.Random.nextString(10)
      val hash       = InMemoryDB hash randomPass
      assert(SecureHash().validatePassword(randomPass, hash))
    }
  }

  test("read json") {
    println(InMemoryDB.users)
  }

}
