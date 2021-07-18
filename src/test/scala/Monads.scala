import scala.language.adhocExtensions
import org.scalacheck.*
import Prop.*
import Arbitrary.arbitrary
import cats.kernel.laws.SemigroupLaws


//  ? === =/= != ==> --> <> |= *** ~> ~~> >>= >>- $> |>

object MonadSpec extends Properties("Monad") {
  import cats.*
  import cats.syntax.all.*
  import cats.data.*

  property("Writer") = forAll { (i: Int) =>
    type Logged[A] = Writer[Int, A]
    val log = for {
      _ <- i.tell
      a <- (i * i).pure[Logged]
      _ <- a.tell
      b <- (a * a).pure[Logged]
      _ <- b.tell
    } yield a + b
    log.value == (i * i) + ((1 to 4).foldLeft(1)((a, _) => a * i)) &&
    log.written == i + log.value
  }

  property("Reader") = forAll { (i: Int) =>
    val a: Reader[Int, String] = Reader(i => i.toString)
    val b: Reader[Int, List[String]] = Reader(i => Range(0, i).toList.map(_.toString))
    val xs = for {
      s <- a
      l <- b
    } yield s :: l

    xs(7) == List("7", "0", "1", "2", "3", "4", "5", "6")
  }

  /**
   * The =PostfixCalc= constains code for postfix calculation
   * @author Ireina
  */
  object PostfixCalc {

    type CalcState[A] = State[List[Int], A]

    def operand(num: Int): CalcState[Int] =
      State[List[Int], Int] { stack =>
        (num :: stack, num)
      }
    def operator(func: (Int, Int) => Int): CalcState[Int] =
      State[List[Int], Int] {
        case b :: a :: tail =>
          val ans = func(a, b)
          (ans :: tail, ans)
        case _ => sys.error("Fail!")
      }
    
    /**
     * Evaluate one step
     * @param sym the symbol: numbers, operators
     * @return a state
    */
    def evalOne(sym: String): CalcState[Int] = 
      sym match {
        case "+" => operator(_ + _) 
        case "-" => operator(_ - _) 
        case "*" => operator(_ * _) 
        case "/" => operator(_ / _) 
        case num => operand(num.toInt)
    }
    def evalAll(input: List[String]): CalcState[Int] = 
      input.foldLeft(0.pure[CalcState]) {
        (state, s) =>
          state *> evalOne(s)
      }
  }

  object StatefulStack {
    type Stack[A] = State[List[A], Option[A]]
    def push[A](a: A) = State.modify((s: List[A]) => a :: s)
    def pop[A]: Stack[A] = State {
      case x :: xs => (xs, Some(x))
      case Nil => (Nil, None)
    }
  }

  property("State") = forAll { (i: Int) => 
    // Let's try to build a monadic stateful stack
    import StatefulStack.*
    val simulation = for {
      _ <- push(1)
      _ <- push(2)
      x <- pop
      _ <- push(3)
      _ <- push(4)
    } yield x

    val checkStack = simulation.runS(Nil).value == List(1, 3, 4).reverse
    val calcCheck = PostfixCalc.evalAll(List("4", "2", "3", "+", "*")).runA(Nil).value == 20

    checkStack && calcCheck
  }

  property("any") = forAll { (i: Int) =>
    given List[Int] = 0 :: 1 :: 2 :: Nil
    def test[A: List](a: A): List[A] = 
      (0 until summon[List[A]].length).map(_ => a).toList
    
    println(test(1))

    type F[B] = Int => B
    val f: F[String] = _.toString
    def from[M[_], A]: A => M[A] = ???

    enum Option[+A] {
      case None
      case Some(a: A)
    }

    true
  }

}


