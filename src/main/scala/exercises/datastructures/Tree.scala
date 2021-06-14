package exercises.datastructures

/*
sealed trait Tree[+A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
*/

enum Tree[+A]:
  case Leaf(value: A)
  case Branch(left: Tree[A], right: Tree[A])


object Tree:

  def size[A](tree: Tree[A]): Int = tree match
    case Leaf(_) => 1
    case Branch(left, right) => size(left) + size(right) + 1

  def maximum(tree: Tree[Int]): Int = tree match
    case Leaf(x) => x
    case Branch(l, r) => maximum(l) max maximum(r)

  def depth[A](tree: Tree[A]): Int = tree match
    case Leaf(_) => 1
    case Branch(l, r) => (depth(l) max depth(r)) + 1

  def map[A, B](tree: Tree[A])(f: A => B): Tree[B] = tree match
    case Leaf(x) => Leaf(f(x))
    case Branch(l, r) => Branch(map(l)(f), map(r)(f))

  def fold[A, B](tree: Tree[A])(f: A => B)(merge: (B, B) => B): B = tree match
    case Leaf(x) => f(x)
    case Branch(l, r) => merge(fold(l)(f)(merge), fold(r)(f)(merge))

end Tree