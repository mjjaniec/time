import sbt.Keys.libraryDependencies

name := "time"

version := "0.4.1"

scalaVersion := "2.12.8"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")

lazy val jars = taskKey[Unit]("Copy jars from modules folders to 'app'")

lazy val commons = project.settings(
  autoScalaLibrary := false
)

val osName: SettingKey[String] = SettingKey[String]("osName")
val jfxVersion = "12.0.1"


lazy val application = project.dependsOn(commons).settings(
  osName := (System.getProperty("os.name") match {
    case l if l.startsWith("Linux") => "linux"
    case m if m.startsWith("Mac") => "mac"
    case w if w.startsWith("Windows") => "win"
    case _ => throw new Exception("Unknown platform!")
  }),
  
  libraryDependencies += "com.avsystem.commons" %% "commons-core" % "1.25.5",
  libraryDependencies += "org.openjfx" % "javafx-base" % jfxVersion classifier osName.value,
  libraryDependencies += "org.openjfx" % "javafx-controls" % jfxVersion classifier osName.value,
  libraryDependencies += "org.openjfx" % "javafx-fxml" % jfxVersion classifier osName.value,
  libraryDependencies += "org.openjfx" % "javafx-graphics" % jfxVersion classifier osName.value,


  assemblyJarName in assembly := "application.jar",
  assemblyOutputPath in assembly := baseDirectory.value / "../jar/application.jar"
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