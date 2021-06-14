package exercises.laziness


enum Stream[+A]:
  case Empty
  case Cons(x: () => A, xs: () => Stream[A])


import Stream.*
extension [A](s: Stream[A]) {

  def headOption: Option[A] = s match
    case Empty => None
    case Cons(x, xs) => Some(x())
  
  def toListRecursive: List[A] = s match
    case Empty => Nil
    case Cons(x, xs) => x() :: xs().toListRecursive

  def toList: List[A] = {
    val buf = new collection.mutable.ListBuffer[A]
    @annotation.tailrec
    def go(s: Stream[A]): List[A] = s match
      case Cons(x, xs) => { buf += x(); go(xs()) }
      case _ => buf.toList
    
    go(s)
  }

  def take(n: Int): Stream[A] = s match
    case Cons(x, xs) if n >  1 => cons(x(), xs().take(n - 1))
    case Cons(x, __) if n == 1 => cons(x(), empty)
    case _ => empty
  
  @annotation.tailrec
  def drop(n: Int): Stream[A] = s match
    case Cons(_, t) if n > 0 => t().drop(n - 1)
    case _ => s

  def takeWhile(f: A => Boolean): Stream[A] = ???

  def exists(p: A => Boolean): Boolean = s match
    case Cons(x, xs) => p(x()) || xs().exists(p)
    case _ => false
  
  def foldRight[B](z: => B)(f: (A, => B) => B): B = s match
    case Cons(x, xs) => f(x(), xs().foldRight(z)(f))
    case _ => z

  def forAll(p: A => Boolean): Boolean = ???
}



object Stream:

  def cons[A](x: => A, xs: => Stream[A]): Stream[A] =
    lazy val head = x
    lazy val tail = xs
    Cons(() => head, () => tail)
  
  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty) empty else cons(as.head, apply(as.tail*))
  
end Stream