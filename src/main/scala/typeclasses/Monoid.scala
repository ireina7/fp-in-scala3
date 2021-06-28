package typeclasses




object Monoids:
  import cats.Monoid 
  import cats.Semigroup
  import cats.syntax.semigroup._

  def test() = {
    //import cats.instances.string._ // for Monoid 
    
    // Get empty
    Monoid[String].empty

    // Combine monoids | semigroups
    Monoid[String].combine("explict ", "combine")
    Semigroup[String].combine("combine ", "semigroup")

    // Semigroup syntax
    println("123" |+| "456")
    println(123 |+| 456)
    println(Option(1) |+| Option(2))
  }

end Monoids