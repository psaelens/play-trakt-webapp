name := """play-kendoui"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

//scalaVersion := "2.11.1"
scalaVersion := "2.10.4"

resolvers += (
    "Local Maven Repository" at "file:///"+Path.userHome.absolutePath+"/.m2/repository"
)

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws, 
  "org.scalatestplus" %% "play" % "1.1.0" % "test",
  "be.spitech.trakt" % "api-client" % "0.0.1-SNAPSHOT",
  "org.webjars" % "bootstrap" % "3.2.0",
  "org.webjars" % "jquery" % "1.11.1",
  "org.webjars" % "kendoui" % "2014.2.903"
)
