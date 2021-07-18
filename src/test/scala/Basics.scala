import scala.language.adhocExtensions
import org.scalacheck.*
import Prop.*
import Arbitrary.arbitrary
import scala.quoted.Quotes


object CustomClassSpec extends Properties("Custom class") {

  case class Rect(width: Int, height: Int) {
    def area: Int = width * height
    def >=(other: Rect) = this.area >= other.area
  }

  val rectGen: Gen[Rect] = for {
    w <- Gen.choose(0, 9999)
    h <- Gen.choose(0, 9999)
  } yield Rect(w, h)

  given Arbitrary[Rect] = Arbitrary(rectGen)

  val collector: PartialFunction[Rect, String] = {
    case r if r.area < 5e6 => "small"
    case r if r.area > 1e7 => "large"
    case _ => "middle"
  }

  property("Rectangle area") = forAll { (r: Rect) =>
    collect(collector(r)) {
      r.area == r.width * r.height // It works!
    }
  }

  property("Bigger than") = forAll { (a: Rect, b: Rect) => 
    a >= b == a.area >= b.area
  }

}


object IntSpec extends Properties("Int") {

  property("addition") = forAll { (n: Int) =>
    (n >= 0 && n < 10000) ==> (List.fill(n)("").length == n)
  }

}

