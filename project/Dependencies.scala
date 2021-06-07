import sbt._

object Dependencies {
  object Libraries {
    val commonsLang3             = Seq("org.apache.commons"          % "commons-lang3").map(_ % Versions.commonsLang3Version)

    val zio = Seq(
      "dev.zio" %% "zio"
    ).map(_ % Versions.zioVersion)
    val sttp = Seq(
      "com.softwaremill.sttp.client3" %% "core",
      "com.softwaremill.sttp.client3" %% "async-http-client-backend-zio",
      "com.softwaremill.sttp.client3" %% "circe"
    ).map(_ % Versions.sttpVersion)
    val circe = Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-generic-extras",
      "io.circe" %% "circe-parser"
    ).map(_ % Versions.circeVersion)

    val zioTest = Seq(
      "dev.zio" %% "zio-test",
      "dev.zio" %% "zio-test-sbt",
      "dev.zio" %% "zio-test-magnolia"
    ).map(_ % Versions.zioVersion % "test")

    val doobie = Seq(
      "org.tpolecat" %% "doobie-core",
      "org.tpolecat" %% "doobie-hikari"
    ).map(_ % Versions.doobieVersion)

  }

  object Versions {
    lazy val awsJavaSDKVersion = "1.11.983"
    lazy val awsJavaSDKVersion2 = "2.16.37"
    lazy val slickVersion = "3.3.3"
    lazy val scalajVersion = "2.4.2"
    lazy val scalatestVersion = "3.2.6"
    lazy val slickPGVersion = "0.19.5"
    lazy val circeVersion = "0.13.0"
    lazy val awsLambdaJavaCoreVersion = "1.2.1"
    lazy val awsLambdaJavaEventsVersion = "3.8.0"
    lazy val slickMigrationApiVersion = "0.8.0"
    lazy val commonsLang3Version = "3.12.0"
    lazy val kinesisStreamReaderVersion = "2.0.0"
    lazy val scanamoVersion = "1.0.0-M15"
    lazy val squantsVersion = "1.7.4"
    lazy val scalaParallelCollectionsVersion = "1.0.1"
    lazy val kindProjectorVersion = "0.10.3"
    lazy val betterMonadicForVersion = "0.3.1"
    lazy val zioVersion = "1.0.5"
    lazy val fdrUtilsVersion = "1.6.0"
    lazy val sttpVersion = "3.1.9"
    lazy val jacksonVersion = "2.12.1"
    lazy val doobieVersion = "0.12.1"
    lazy val zioInteropCats = "2.4.0.0"
    lazy val oracle = "11.2.0.4"
  }

}
