package route

package object implementation:

  extension[A] (value: A)
    def withQuotes: String = s"`$value`"

