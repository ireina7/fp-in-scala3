package exercises.parallelism

import java.util.concurrent._
import language.implicitConversions


object Par:

  type Par[A] = ExecutorService => Future[A]

  def run[A](s: ExecutorService)(a: Par[A]): Future[A] = a(s)

  //def unit[A](a: A): Par[A] = (es: ExecutorService) => UnitFuture(a)
end Par