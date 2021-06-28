package exercises.monads


trait Functor[F[_]]:
  extension [A](fa: F[A])
    def map[B](f: A => B): F[B]

trait Applicative[F[_]](using ev: Functor[F]) extends Functor[F]:
  def pure[A](x: A): F[A]
  extension [A](fa: F[A])
    def <*>[B](f: F[A => B]): F[B]
    def map[B](f: A => B): F[B] = ev.map(fa)(f)

/*
trait Monad[F[_]](using ev: Applicative[F]) extends Applicative[F]:
  def pure[A](x: A): F[A] = ev.pure(x)
  extension [A](fa: F[A])
    def flatMap[B](f: A => F[B]): F[B]
    def <*>[B](f: F[A => B]): F[B] = ev.<*>(fa)(f)
*/

trait Monad[F[_]]:
  def pure[A](x: A): F[A]
  extension [A](fa: F[A])
    def flatMap[B](f: A => F[B]): F[B]




// Hypothetical example. This won't actually compile:
def compose[M1[_]](using ev1: Monad[M1], ev2: Monad[Option]) =
  type Composed[A] = M1[Option[A]]

  new Monad[Composed]:

    def pure[A](a: A): Composed[A] =
      ev1.pure(ev2.pure(a))

    extension [A](m1_opt_a: Composed[A])
      def flatMap[B](f: A => Composed[B]): Composed[B] = ??? // missing
        // ev1.flatMap(m1_opt_a)(opt_a => opt_a.map(f).getOrElse(ev1.pure(None))) //no way out!

end compose

