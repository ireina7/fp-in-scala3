
lazy val root = project
  .in(file("."))
  .settings(
    name := "fp-in-scala3",
    version := "0.1.0",

    scalaVersion := V.scala3Version,
    scalacOptions += "-source:future",

    libraryDependencies ++= Seq(
      Library.catsCore,
      Library.catsFree,
      Library.catsLaws,
      Library.catsEffect,

      Library.junit      % Test,
      Library.scalaTest  % Test,
      Library.scalaCheck % Test,
      // compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
    )
  )

