name := """play-kendoui"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

//scalaVersion := "2.11.1"
scalaVersion := "2.10.4"


libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws, 
  "org.webjars" % "bootstrap" % "3.2.0",
  "com.squareup.retrofit" % "retrofit" % "1.6.1",
  "org.apache.commons" % "commons-collections4" % "4.0",
  "org.webjars" % "jquery" % "1.11.1"
)
