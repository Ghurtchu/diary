package object model {
  
  extension[A](a: A)
    def some: Option[A] = Some(a)
}
