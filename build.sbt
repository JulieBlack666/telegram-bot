name := "telegram-bot"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies += "info.mukel" %% "telegrambot4s" % "3.0.14"