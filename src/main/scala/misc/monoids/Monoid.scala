package exercises.monoids


trait Monoid[A]:
  def op(a1: A, a2: A): A
  def zero: A
  extension (a: A) def |+| (b: A) = op(a, b)


given Monoid[String] with {
  def op(a: String, b: String) = a + b
  def zero = ""
}


object Monoid:

  val stringMonoid = new Monoid[String]:
    def op(a1: String, a2: String) = a1 + a2
    val zero = ""
  
  def listMonoid[A] = new Monoid[List[A]]:
    def op(a1: List[A], a2: List[A]) = a1 ++ a2
    val zero = Nil
  
  val intAddition = new Monoid[Int]:
    def op(x: Int, y: Int) = x + y
    val zero = 0
  
  val intMultiplication = new Monoid[Int]:
    def op(x: Int, y: Int) = x * y
    val zero = 1
  
  val booleanOr = new Monoid[Boolean]:
    def op(x: Boolean, y: Boolean) = x || y
    val zero = false
  
  val booleanAnd = new Monoid[Boolean]:
    def op(x: Boolean, y: Boolean) = x && y
    val zero = true

  def optionMonoid[A] = new Monoid[Option[A]] {
    def op(x: Option[A], y: Option[A]) = x orElse y
    val zero = None
  }

  // Instances
  given Monoid[String] = stringMonoid
  given [A]: Monoid[List[A]] = listMonoid
  given intAdditionMonoid: Monoid[Int] = intAddition
  given intMultiplyMonoid: Monoid[Int] = intMultiplication
  given boolOrMonoid: Monoid[Boolean] = booleanOr
  given boolAndMonoid: Monoid[Boolean] = booleanAnd
  given [A]: Monoid[Option[A]] = optionMonoid



  def usingMonoid[A](a: A, b: A)(using Monoid[A]) = 
    a |+| b

  def test() = {
    val x = usingMonoid(2, 3)(using intMultiplication)
    println(x)
  }

end Monoid