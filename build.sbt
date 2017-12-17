name := "time"

version := "0.2.3"

scalaVersion := "2.12.4"

lazy val core = project.settings(
  assemblyJarName in assembly := "core.jar"
)

lazy val updater = project.settings(
  libraryDependencies += "com.squareup.retrofit2" % "retrofit" % "2.3.0",
  libraryDependencies += "com.squareup.retrofit2" % "converter-gson" % "2.3.0",
  autoScalaLibrary := false,
  mainClass in Compile := Some("com.github.mjjaniec.time.updater.Main"),
  assemblyJarName in assembly := "updater.jar"
)

lazy val root = project.in(file("."))
  .aggregate(core, updater)