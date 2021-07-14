package fp

//import simulacrum.typeclass


/* 
- Subtyping
1. Easily extend data
2. Pre-binding
3. Only rank-1
4. Modular

- Typeclass
1. Easily extend function
2. Post-binding
3. Including rank-2
4. Needing open class

- Ideas
1. Prefer typeclasses (cats!)
2. Trait-Typeclass dual pattern to implement typeclass to mutable situations
3. Use trait and subtyping to build typeclass system
4. Use subtyping to descibe large pure data or mutable states
*/

import cats.*
import cats.syntax.all.*
import cats.laws.*
import cats.effect.{
  IO, IOApp,
  ExitCode,
  Resource,
}

object Application extends IOApp:

  val program: IO[Unit] = for {
    _ <- IO.println("Enter your name:")
    n <- IO.readLine
    _ <- IO.println(s"Hello, $n")
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = {
    program *>
    IO.println("This is the functional world of Scala!")
      .as(ExitCode.Success)
  }
end Application






def tests(): Unit = {

  traits.Monoids.test()
  traits.Functors.test()
  traits.Monads.test()
  traits.Transformers.test()
  traits.free.FreeDataStructures.test()
  traits.free.CoyonedaTest.test()
}

