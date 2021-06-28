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


@main def main: Unit = 
  println("Hello! This is the functional world of Scala3.")

  typeclasses.Monoids.test()
  typeclasses.Functors.test()
  typeclasses.Monads.test()
  typeclasses.Transformers.test()
  typeclasses.free.FreeDataStructures.test()
  typeclasses.free.CoyonedaTest.test()

end main



