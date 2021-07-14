val scala3Version = "3.0.0"
val catsVersion = "2.6.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "fp-in-scala3",
    version := "0.1.0",

    scalaVersion := scala3Version,
    scalacOptions += "-source:future",

    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core"       % catsVersion,
      "org.typelevel" %% "cats-free"       % catsVersion,
      "org.typelevel" %% "cats-laws"       % catsVersion,
      "org.typelevel" %% "cats-effect"     % "3.1.1",

      "com.novocode"  %  "junit-interface" % "0.11"  % Test,
      "org.scalatest" %% "scalatest"       % "3.2.9" % Test,
      // compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
    )
  )

