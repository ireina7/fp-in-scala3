package traits.free


enum Freer[F[_], A]:
  case Pure(a: A)
  case Impure[F[_], A, B](fa: F[A], k: A => Freer[F, B]) extends Freer[F, B]

  def flatMap[B](f: A => Freer[F, B]): Freer[F, B] = this match
    case Pure(a) => f(a) 
    case Impure(fa, k) => Impure(fa, a => k(a).flatMap(f))

end Freer




object FreerDataStructures:

  def test(): Unit = {
    println("Freer!")
  }

end FreerDataStructures