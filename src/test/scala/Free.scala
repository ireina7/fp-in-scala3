import scala.language.adhocExtensions
import org.scalacheck.*
import Prop.*
import Arbitrary.arbitrary


object FreeSpec extends Properties("Free structures"):

  property("Free monad") = forAll {
    (_: Int) =>
      true
  }

end FreeSpec