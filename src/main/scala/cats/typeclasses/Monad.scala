package cats.typeclasses

import cats.Monad
import cats.catsInstancesForId

object Monads:
  import cats.syntax.applicative._
  import cats.syntax.flatMap._
  import cats.syntax.functor._

  def testBasics(): Unit = {
    val opt1 = Monad[Option].pure(3)
    val opt2 = Monad[Option].flatMap(opt1)(a => Some(a + 2))
    val opt3 = Monad[Option].map(opt2)(a => 100 * a)
  }

  def testId(): Unit = {
    def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] = 
      for {
        x <- a
        y <- b
      } yield x * x + y * y
    
    // Id monad
    import cats.Id
    import cats.catsInstancesForId
    println(sumSquare(3: Id[Int], 4: Id[Int]))
  }

  def testEither(): Unit = {
    // Either monad
    val either1: Either[String, Int] = Right(10)
    val either2: Either[String, Int] = Right(32)
    val epe = for {
      a <- either1
      b <- either2
    } yield a + b
    println(epe)
  }

  def testEval() = {
    // Eval monad
    import cats.Eval
    val now = Eval.now(math.random + 1000)
    val later = Eval.later(math.random + 2000)
    val always = Eval.always(math.random + 3000)

    val greeting = Eval
      .always { println("Step 1"); "Hello" }
      .map { str => println("Step 2"); s"$str world" }

    println(greeting.value)
  }

  def testWriter() = {
    // Writer monad
    import cats.data.Writer
    import cats.syntax.writer.*
    import cats.catsInstancesForId

    Writer(Vector(
      "It was the best of times",
      "it was the worst of times"
    ), 1859)

    type Logged[V] = Writer[Vector[String], V]

    val test = for {
      a <- 1.pure[Logged]
      _ <- Vector("Init").tell
      b <- 32.writer(Vector("add 32"))
    } yield a + b

    println(test)

    def fact(n: Int): Logged[Int] = 
      if n <= 0 then 1.pure[Logged] else for {
        x <- fact(n - 1).map(_ * n)
        _ <- Vector(s"fact($n) = $x").tell
      } yield x
    end fact

    println(fact(5))
  }

  def testReader() = {
    import cats.data.Reader
    case class Cat(name: String, favoriteFood: String)
    // defined class Cat
    val catName: Reader[Cat, String] = Reader(cat => cat.name)
    val greetKitty: Reader[Cat, String] = catName.map(name => s"Hello ${name}")
    val feedKitty: Reader[Cat, String] = Reader(cat => s"Have a nice bowl of ${cat.favoriteFood}")
    val greetAndFeed: Reader[Cat, String] =
    for {
      greet <- greetKitty
      feed <- feedKitty
    } yield s"$greet. $feed."
  }

  def testState() = {
    import cats.data.State
    val a = State[Int, String] { state =>
      (state, s"The state is $state")
    }
    val (state, result) = a.run(10).value
    val s = a.runS(10).value
    val r = a.runA(10).value
  }

  def test(): Unit = {
    
    testBasics()
    testId()
    testEither()
    testEval()
    testWriter()
    testReader()
    testState()
  }

end Monads