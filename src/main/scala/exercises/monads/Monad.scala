package exercises.monads


trait Functor[F[_]]:
  extension [A](fa: F[A])
    def map[B](f: A => B): F[B]

trait Applicative[F[_]](using ev: Functor[F]) extends Functor[F]:
  def pure[A](x: A): F[A]
  extension [A](fa: F[A])
    def <*>[B](f: F[A => B]): F[B]
    def map[B](f: A => B): F[B] = ev.map(fa)(f)


trait Monad[F[_]](using ev: Applicative[F]) extends Applicative[F]:
  def pure[A](x: A): F[A] = ev.pure(x)
  extension [A](fa: F[A])
    def flatMap[B](f: A => F[B]): F[B]
    def <*>[B](f: F[A => B]): F[B] = ev.<*>(fa)(f)

