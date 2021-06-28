package typeclasses.free

import cats.Functor


enum Free[F[_], A]:
  case Pure(a: A)
  case Impure(ffree: F[Free[F, A]])

  def flatMap[B](f: A => Free[F, B])(using ev: Functor[F]): Free[F, B] = this match
    case Pure(a) => f(a) 
    case Impure(ffree) => Impure(ev.map(ffree)(free => free.flatMap(f)))

  def map[B](f: A => B)(using ev: Functor[F]): Free[F, B] = flatMap(a => Pure(f(a)))
end Free

object Free:

  def lift[F[_], A](fa: F[A])(using F: Functor[F]): Free[F, A] = 
    Impure(F.map(fa)(a => Pure(a)))


end Free



/*Free

tell("1").flatMap(x => tell("2").map(y => x + y))

Impure(Tell("1", 1).map(a => Pure(a))).flatMap(x => tell("2").map(y => x + y))

Impure(Tell("1", Pure(1))).flatMap(x => tell("2").map(y => x + y))

Impure(Tell("1", Pure(1)).map(free => free.flatMap(x => tell("2").map(y => x + y))))

Impure(Tell("1", Pure(1).flatMap(x => tell("2").map(y => x + y))))

Impure(Tell("1", tell("2").map(y => 1 + y))) // x = 1

Impure(Tell("1", Impure(Tell("2", Pure(2))).map(y => 1 + y)))

Impure(Tell("1", Impure(Tell("2", Pure(2))).flatMap(y => Pure(1 + y))))

Impure(Tell("1", Impure(Tell("2", Pure(2)).map(free => free.flatMap(y => Pure(1 + y))))))

Impure(Tell("1", Impure(Tell("2", Pure(2).flatMap(y => Pure(1 + y))))))

Impure(Tell("1", Impure(Tell("2", Pure(1 + 2))))) // y = 2

Impure(Tell("1", Impure(Tell("2", Pure(3)))))


*/




object FreeDataStructures:
  import Free.*

  type Writer[W, A] = Free[[T] =>> Tell[W, T], A]

  case class Tell[W, A](w: W, a: A) {
    def map[B](f: A => B) = Tell(w, f(a))
  }

  given [W]: Functor[[T] =>> Tell[W, T]] with
    def map[A, B](fa: Tell[W, A])(f: A => B): Tell[W, B] = fa.map(f)

  def tell[W](w: W): Writer[W, Unit] = Free.lift(Tell(w, ()))
  //def get[W]: Writer[W, W] = Free.lift(Tell())

  def runAsList[W, A](free: Writer[W, A]): (List[W], A) = free match {
    case Pure(a) => (Nil, a)
    case Impure(Tell(w, free)) => runAsList(free) match
      case (ws, a) => (w :: ws, a)
  }

  def runAsVec[W, A](free: Writer[W, A]): (Vector[W], A) = {
    def go(acc: Vector[W], free: Writer[W, A]): (Vector[W], A) =
      free match {
        case Pure(a) => (acc, a)
        case Impure(Tell(w, free)) => go(acc :+ w, free)
      }

    go(Vector.empty, free)
  }



  def test(): Unit = {
    println("free!")

    val result = for {
      _ <- tell("hoge")
      _ <- tell("fuga")
    } yield ()

    println(runAsList(result))
    println(runAsVec(result))

  }

end FreeDataStructures