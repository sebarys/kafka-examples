ThisBuild / scalaVersion := "2.12.10"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.sebarys"
ThisBuild / organizationName := "app"

lazy val root = (project in file("."))
  .settings(
    name := "kafka",
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "com.typesafe" % "config" % "1.4.0",
      "org.apache.kafka" %% "kafka-streams-scala" % "2.3.1",
      "org.scalatest" %% "scalatest" % "3.0.8" % Test
    )
  )