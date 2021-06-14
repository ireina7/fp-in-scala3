package exercises.errorhandling

// hide std library `Option` and `Either`, since we are writing our own in this chapter
import scala.{ Option => _, Either => _, _ }


enum Option[+A]:
  case None
  case Some(a: A)

  def map[B](f: A => B): Option[B] = this match
    case None => None
    case Some(a) => Some(f(a))

  def flatMap[B](f: A => Option[B]): Option[B] = this match
    case None => None
    case Some(x) => f(x)

  def getOrElse[B >: A](default: => B): B = this match
    case None => default
    case Some(x) => x
  
  def orElse[B >: A](ob: => Option[B]): Option[B] = 
    this map (Some(_)) getOrElse ob

  def filter(f: A => Boolean): Option[A] = 
    this.flatMap(x => if f(x) then Some(x) else None)

end Option
