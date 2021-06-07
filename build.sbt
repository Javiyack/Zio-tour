import Dependencies._

ThisBuild / scalaVersion := "2.13.6"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "tour",
    libraryDependencies ++=
      Libraries.zio ++
        Libraries.zioTest ++
        Libraries.sttp ++
        Libraries.circe ++
        Libraries.doobie ++
        Libraries.commonsLang3,
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )