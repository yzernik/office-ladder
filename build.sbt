import sbt.Project.projectToRef

name := """office-ladder"""

version := "1.0-SNAPSHOT"

lazy val clients = Seq(client)
lazy val scalaV = "2.11.5"

lazy val server = (project in file("server")).settings(
  scalaVersion := scalaV,
  scalaJSProjects := clients,
  pipelineStages := Seq(scalaJSProd),
  resolvers += "Atlassian Releases" at "https://maven.atlassian.com/public/",
  libraryDependencies ++= Seq(
    jdbc,
    anorm,
    "com.mohiva" %% "play-silhouette" % "2.0",
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

def macroParadisePlugin =
  compilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full)

lazy val client = (project in file("client")).settings(
  scalaVersion := scalaV,
  persistLauncher := true,
  persistLauncher in Test := false,
  unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value),
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.0",
    "com.github.japgolly.fork.monocle" %%% "monocle-macro" % "1.1.0",
    "com.github.japgolly.scalajs-react" %%% "core" % "0.8.3",
    "com.github.japgolly.scalajs-react" %%% "ext-scalaz71" % "0.8.3",
    "com.github.japgolly.scalajs-react" %%% "ext-monocle" % "0.8.3",
    "com.github.japgolly.scalajs-react" %%% "extra" % "0.8.3",
    "be.doeraene" %%% "scalajs-jquery" % "0.8.0",
    "com.lihaoyi" %%% "upickle" % "0.2.8"
  ),
  addCompilerPlugin(macroParadisePlugin),
  jsDependencies += "org.webjars" % "react" % "0.12.1" / "react-with-addons.js" commonJSName "React",
  skip in packageJSDependencies := false).
  enablePlugins(ScalaJSPlugin, ScalaJSPlay)

// loads the jvm project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value
