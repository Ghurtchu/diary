package route

package object service {

  extension[A] (value: A)
    def withQuotes: String = s"`$value`"

}
