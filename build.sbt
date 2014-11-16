name := "befolkningsapi"

version := "1.0"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-ws" % "2.4.0-M1",
  "com.typesafe.akka" %% "akka-actor" % "2.3.7",
  "org.specs2" %% "specs2" % "2.4.2" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.7" % "test"
)
