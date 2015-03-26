import sbt.Project.projectToRef

name := """office-ladder"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
lazy val clients = Seq()
lazy val scalaV = "2.11.5"

lazy val server = (project in file("server")).settings(
  scalaVersion := scalaV,
  pipelineStages := Seq(scalaJSProd, gzip),
  libraryDependencies ++= Seq(
    jdbc,
    anorm,
    "com.mohiva" %% "play-silhouette" % "1.0",
    "com.typesafe.play" %% "play-slick" % "0.8.1",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "postgresql" % "postgresql" % "9.1-901.jdbc4",
    "com.github.tototoshi" %% "slick-joda-mapper" % "1.2.0",
    "com.vmunier" %% "play-scalajs-scripts" % "0.1.0",
    "org.webjars" % "jquery" % "1.11.1"
  ),
  EclipseKeys.skipParents in ThisBuild := false).
  enablePlugins(PlayScala).
  aggregate(clients.map(projectToRef): _*)

// loads the jvm project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value
