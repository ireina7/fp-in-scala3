package typeclasses

import cats.Functor


/**
 * An example of giving Functor[Test]
*/
case class Test[A](i: A):
  def id = i

given Functor[Test] with
  def map[A, B](v: Test[A])(f: A => B): Test[B] = Test(f(v.id))


/**
 * Contravariant Functor
*/
trait Printable[A] { self =>
  def format(a: A): String
  def contramap[B](f: B => A): Printable[B] = new Printable[B]:
    def format(b: B): String = self.format(f(b))
}

given Printable[String] with
  def format(s: String) = s

given [A](using ev: Printable[A]): Printable[Test[A]] with
  def format(ta: Test[A]) = ev.contramap[Test[A]](_.id).format(ta)




object Functors:

  def test(): Unit = {
    import cats.syntax.functor._

    val func1: Int => Double = x => x.toDouble
    val func2: Double => Double = y => y * 2

    val mapF = (func1 map func2)(1) 
    val andThenF = (func1 andThen func2)(1)
    assert(mapF == andThenF)

    val list1 = List(1, 2, 3)

    val list2 = Functor[List].map(list1)(_ * 2)
    val option1 = Option(123)
    val option2 = Functor[Option].map(option1)(_.toString)

    val func = (x: Int) => x + 1
    val liftedFunc = Functor[Option].lift(func)
    liftedFunc(Option(1))

    val test = Test(5)
    println(test map (_ * 2))

    println(summon[Printable[Test[String]]].format(Test("Testing contra functor")))

  }

end Functors