package exercises.errorhandling

// hide std library `Option` and `Either`, since we are writing our own in this chapter
import scala.{ Option => _, Either => _, _ }


enum Option[+A]:
  case None
  case Some(a: A)

  def map[B](f: A => B): Option[B] = this match
    case None => None
    case Some(a) => Some(f(a))
  
end Option
