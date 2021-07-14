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
  IO, IOApp
}

object HelloWorld extends IOApp.Simple {

  val run = IO.println("Hello, World!")
}






def tests(): Unit = {

  traits.Monoids.test()
  traits.Functors.test()
  traits.Monads.test()
  traits.Transformers.test()
  traits.free.FreeDataStructures.test()
  traits.free.CoyonedaTest.test()
}

