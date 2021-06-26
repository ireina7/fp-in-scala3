package cats.typeclasses

import cats.syntax.applicative._ // for pure
import cats.Monad
import cats.data.OptionT
//import javax.xml.transform.Transformer

// Build our final monad stack using OptionT:
type ErrorOrOption[A] = OptionT[[T] =>> Either[String, T], A] // Scala3 type lambda!


object Transformers:

  def test() = {

  }

end Transformers