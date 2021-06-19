package cats.typeclasses

import cats.Monad


object Monads:

  def test(): Unit = {
    import cats.syntax.applicative._
    import cats.syntax.flatMap._
    import cats.syntax.functor._

    val opt1 = Monad[Option].pure(3)
    val opt2 = Monad[Option].flatMap(opt1)(a => Some(a + 2))
    val opt3 = Monad[Option].map(opt2)(a => 100 * a)

    def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] = 
      for {
        x <- a
        y <- b
      } yield x * x + y * y
    
    // Id monad
    import cats.Id
    import cats.catsInstancesForId
    println(sumSquare(3: Id[Int], 4: Id[Int]))

    // Either monad
    val either1: Either[String, Int] = Right(10)
    val either2: Either[String, Int] = Right(32)
    val epe = for {
      a <- either1
      b <- either2
    } yield a + b
    println(epe)

    // Eval monad
    import cats.Eval
    val now = Eval.now(math.random + 1000)
    val later = Eval.later(math.random + 2000)
    val always = Eval.always(math.random + 3000)

    val greeting = Eval
      .always { println("Step 1"); "Hello" }
      .map { str => println("Step 2"); s"$str world" }

    println(greeting.value)

    // Writer monad
  }

end Monads