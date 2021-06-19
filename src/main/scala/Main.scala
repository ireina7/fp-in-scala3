package fp

//import simulacrum.typeclass


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

end main



