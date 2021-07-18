import scala.language.adhocExtensions
import org.scalacheck.*
import Prop.*
import Arbitrary.arbitrary


object IOSpec extends Properties("IO properties"):
  import cats.*
  import cats.syntax.all.*
  import cats.effect.*
  import cats.effect.syntax.all.*
  import cats.effect.unsafe.implicits.global

  property("unknown") = forAll {
    (x: Int) => {

      // val cs1 = IO.contextShift(ec1)
      // val cs2 = IO.contextShift(ec2)

      def query(msg: String): IO[String] = for {
        _ <- IO.println(msg)
        x <- IO.readLine
      } yield x

      def shows(s: String): IO[Unit] = for {
        _ <- IO.println(s)
        // _ <- shows(s)
      } yield ()


      val printThread: IO[Unit] =
        IO.executionContext.flatMap(IO.println(_))

      val x = for {
        _ <- printThread //io-compute-1
        _ <- printThread.evalOn(scala.concurrent.ExecutionContext.global) //some-ec-thread
        _ <- printThread //io-compute-1
      } yield ()

      val ioApp = for {
        _ <- shows("1")
        _ <- printThread
        _ <- x
      } yield ()
      
      // Reader.local
      ioApp.unsafeRunSync()
      true
    }
  }

end IOSpec



object Test {
  import cats.*
  import cats.syntax.all.*
  import cats.effect.*
  import cats.effect.syntax.all.*
  import cats.effect.unsafe.implicits.global

  import concurrent.duration.DurationInt

  def shows(s: String): IO[Unit] = IO.println(s)

  def slow: IO[Unit] = for {
    _ <- IO.sleep(5.seconds)
  } yield ()

  def test() = {
    val printThread: IO[Unit] =
      IO.executionContext.flatMap(IO.println(_))

    val x = for {
      _ <- printThread
      _ <- (slow *> printThread).evalOn(scala.concurrent.ExecutionContext.global)
      _ <- printThread
    } yield ()

    val ioApp = for {
      _ <- shows("start!")
      _ <- printThread
      _ <- (shows("async") *> x).start
    } yield ()
    
    // Reader.local
    ioApp.unsafeRunSync()
  }
}