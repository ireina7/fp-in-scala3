package typeclasses.free


enum Coyoneda[F[_], A]:
  case FMap[F[_], A, B](fa: F[A], k: A => B) extends Coyoneda[F, B]

  def map[B](f: A => B): Coyoneda[F, B] = this match
    case FMap(fi, k) => FMap(fi, k andThen f)

end Coyoneda


object Coyoneda:

  def lift[F[_], A](fa: F[A]): Coyoneda[F, A] = Coyoneda.FMap(fa, a => a)

end Coyoneda



object CoyonedaTest:

  def test(): Unit = {
    println("Coyoneda!")
  }

end CoyonedaTest
