name := "time"

version := "0.3.3"

scalaVersion := "2.12.4"

lazy val jars = taskKey[Unit]("Copy jars from modules folders to 'app'")

lazy val application = project.settings(
  libraryDependencies += "com.avsystem.commons" %% "commons-core" % "1.25.5",

  assemblyJarName in assembly := "application.jar",
  assemblyOutputPath in assembly := baseDirectory.value / "../jar/application.jar"
)

lazy val commons = project.settings(
  autoScalaLibrary := false
)

lazy val updater = project.dependsOn(commons).settings(
  libraryDependencies += "com.squareup.retrofit2" % "retrofit" % "2.3.0",
  libraryDependencies += "com.squareup.retrofit2" % "converter-gson" % "2.3.0",

  autoScalaLibrary := false,
  mainClass in Compile := Some("com.github.mjjaniec.time.updater.Main"),
  assemblyOutputPath in assembly := baseDirectory.value / "../jar/updater.jar"
)

lazy val runner = project.dependsOn(commons).settings(
  autoScalaLibrary := false,
  mainClass in Compile := Some("com.github.mjjaniec.time.runner.Main"),
  assemblyOutputPath in assembly := baseDirectory.value / "../jar/runner.jar"
)


lazy val root = project.in(file("."))
  .aggregate(application, updater, runner)