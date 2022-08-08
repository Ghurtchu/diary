ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.3"

libraryDependencies ++= Seq(
  "io.d11"  %% "zhttp" % "2.0.0-RC10",
  "dev.zio" %% "zio" % "2.0.0",
  "dev.zio" %% "zio-json" % "0.3.0-RC10"
)

lazy val root = (project in file("."))
  .settings(
    name := "zio-notes-rest-api"
  )
