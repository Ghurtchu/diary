package object domain:
  
  extension[A](a: A)
    def some: Option[A] = Some(a)
