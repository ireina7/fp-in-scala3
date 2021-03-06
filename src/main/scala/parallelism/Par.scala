package parallelism

import java.util.concurrent.*
import language.implicitConversions
import scala.util.Success
import scala.util.Failure


object Par:

  type Par[A] = ExecutorService => Future[A]

  def run[A](s: ExecutorService)(a: Par[A]): Future[A] = a(s)

  // `unit` is represented as a function that returns a `UnitFuture`, 
  // which is a simple implementation of `Future` that just wraps a constant value. 
  // It doesn't use the `ExecutorService` at all. It's always done and can't be cancelled. 
  // Its `get` method simply returns the value that we gave it.
  def unit[A](a: A): Par[A] = (es: ExecutorService) => UnitFuture(a)

  private case class UnitFuture[A](get: A) extends Future[A] {
    def isDone = true
    def get(timeout: Long, units: TimeUnit) = get
    def isCancelled = false
    def cancel(evenIfRunning: Boolean): Boolean = false
  }

  // `map2` doesn't evaluate the call to `f` in a separate logical thread, 
  // in accord with our design choice of having `fork` be the sole function in the API for controlling parallelism. 
  // We can always do `fork(map2(a,b)(f))` if we want the evaluation of `f` to occur in a separate thread.
  def map2[A,B,C](a: Par[A], b: Par[B])(f: (A,B) => C): Par[C] = 
    (es: ExecutorService) => {
      val af = a(es)
      val bf = b(es)
      UnitFuture(f(af.get, bf.get)) // This implementation of `map2` does _not_ respect timeouts. It simply passes the `ExecutorService` on to both `Par` values, waits for the results of the Futures `af` and `bf`, applies `f` to them, and wraps them in a `UnitFuture`. In order to respect timeouts, we'd need a new `Future` implementation that records the amount of time spent evaluating `af`, then subtracts that time from the available time allocated for evaluating `bf`.
    }

  // This is the simplest and most natural implementation of `fork`, 
  // but there are some problems with it--for one, the outer `Callable` will block waiting for the "inner" task to complete. 
  // Since this blocking occupies a thread in our thread pool, or whatever resource backs the `ExecutorService`, 
  // this implies that we're losing out on some potential parallelism. 
  // Essentially, we're using two threads when one should suffice. 
  // This is a symptom of a more serious problem with the implementation, and we will discuss this later in the chapter.
  def fork[A](a: => Par[A]): Par[A] = 
    es => es.submit(new Callable[A] {
      def call = a(es).get
    })

  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

  def asyncF[A,B](f: A => B): A => Par[B] =
    a => lazyUnit(f(a))

  def map[A,B](pa: Par[A])(f: A => B): Par[B] =
    map2(pa, unit(()))((a,_) => f(a))

  
  def test(): Unit = {
    import scala.concurrent.{ Await, Future }
    import scala.concurrent.ExecutionContext.Implicits.global
    val future: Future[Int] = Future {
      for (i <- 0 to 5) do
        Thread.sleep(1000)
        println(s"$i: Hello from future")
      7
    }
    future.onComplete {
      case Success(value) => println(s"Finished, get: $value")
      case Failure(error) => println(s"An abominable error has occured: ${error.getMessage}")
    }
    for (i <- 0 to 5) do
      Thread.sleep(1000)
      println(s"$i: Next")
  }

end Par