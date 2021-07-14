package data

// hide std library `Option` and `Either`, since we are writing our own in this chapter
import scala.{ Option as _, Either as _, * }


enum Either[+E, +A]:
  case Left (get: E)
  case Right(get: A)

  def map[B](f: A => B): Either[E, B] = this match
    case Left(err) => Left(err)
    case Right(xs) => Right(f(xs))

  def flatMap[EE >: E, B](f: A => Either[EE, B]): Either[EE, B] = this match
    case Left(err) => Left(err)
    case Right(xs) => f(xs)

  def orElse[EE >: E, B >: A](b: => Either[EE, B]): Either[EE, B] = this match
    case Left(_) => b
    case Right(xs) => this

  def map2[EE >: E, B, C](b: Either[EE, B])(f: (A, B) => C): Either[EE, C] = 
    for { a <- this; b1 <- b } yield f(a, b1)

end Either