ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.3"

libraryDependencies ++= Seq(
  "io.d11"  %% "zhttp" % "2.0.0-RC10",
  "dev.zio" %% "zio" % "2.0.0",
  "dev.zio" %% "zio-json" % "0.3.0-RC10"
)
libraryDependencies += "io.github.nremond" %% "pbkdf2-scala" % "0.7.0"
libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
libraryDependencies += "com.github.jwt-scala" %% "jwt-circe" % "9.0.6"

lazy val root = (project in file("."))
  .settings(
    name := "zio-notes-rest-api"
  )
