name := """play_test"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)
libraryDependencies += "commons-io" % "commons-io" % "2.4"
libraryDependencies += "com.auth0" % "java-jwt" % "3.1.0"

