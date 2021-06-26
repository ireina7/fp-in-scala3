package fp

//import simulacrum.typeclass


import cats.Monoid
import cats.syntax.monoid.*
import simulacrum.typeclass

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


@main def main: Unit = 
  println("Hello! This the functional world of Scala3.")

  // exercises
  import exercises.*
  gettingstarted.MyModule.test()
  gettingstarted.FormatAbsAndFactorial.test()
  gettingstarted.TestFib.test()
  gettingstarted.AnonymousFunctions.test()
  //exercises.parallelism.Par.test()
  monoids.Monoid.test()

  // cats study
  import cats.*
  typeclasses.Monoids.test()
  typeclasses.Functors.test()
  typeclasses.Monads.test()
  typeclasses.Transformers.test()
  typeclasses.free.FreeDataStructures.test()

end main



