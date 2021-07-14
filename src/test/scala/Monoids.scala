import scala.language.adhocExtensions
import org.scalacheck.*
import Prop.*
import Arbitrary.arbitrary


object MonoidSpec extends Properties("Monoids") {
  import cats.*
  import cats.syntax.all.*
  import cats.data.*


  trait SemigroupLaws {
    def associative[T: Monoid: Arbitrary] = 
      forAll { 
        (a: T, b: T, c: T) => 
          ((a |+| b) |+| c) == (a |+| (b |+| c))
      }
  }

  trait MonoidLaws extends SemigroupLaws {
    def unit[T: Monoid: Arbitrary] = forAll { 
      (a: T) => {
        val unit = Monoid[T].empty
        (a |+| unit) == (unit |+| a)
      }
    }
  }

  val laws = new MonoidLaws {}
  property("Associative law") = laws.associative[String]
  property("Empty unit law" ) = laws.unit[String]

}