import sbt._

object V {

  val scala3Version = "3.0.0"
  val cats = "2.6.1"
  val catsEffect = "3.1.1"
  val junit = "0.11"
  val scalaTest = "3.2.9"
  val scalaCheck = "1.15.4"
}

object Library {

  val catsCore   = "org.typelevel"  %% "cats-core"       % V.cats
  val catsLaws   = "org.typelevel"  %% "cats-laws"       % V.cats
  val catsFree   = "org.typelevel"  %% "cats-free"       % V.cats
  val catsEffect = "org.typelevel"  %% "cats-effect"     % V.catsEffect

  val junit      = "com.novocode"   %  "junit-interface" % V.junit
  val scalaTest  = "org.scalatest"  %% "scalatest"       % V.scalaTest
  val scalaCheck = "org.scalacheck" %% "scalacheck"      % V.scalaCheck
}