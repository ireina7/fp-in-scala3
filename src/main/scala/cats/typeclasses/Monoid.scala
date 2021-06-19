package cats.typeclasses

import cats.Monoid 
import cats.Semigroup


object Monoids:
  import cats.syntax.semigroup._

  def test() = {
    //import cats.instances.string._ // for Monoid 
    
    Monoid[String].empty
    Monoid[String].combine("explict ", "combine")
    Semigroup[String].combine("combine ", "semigroup")
    println("123" |+| "456")
    println(123 |+| 456)
    println(Option(1) |+| Option(2))
  }

end Monoids