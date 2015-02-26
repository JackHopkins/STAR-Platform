name := "template"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"


libraryDependencies ++= Seq(
  javaCore,
  javaJdbc,
  javaEbean,
  cache,
  "com.newrelic.agent.java" % "newrelic-api" % "3.1.1",
  "com.google.guava" % "guava" % "14.0",
   "ws.securesocial" %% "securesocial" % "master-SNAPSHOT",
  "se.digiplant" %% "play-res" % "1.1.1",
"com.github.mumoshu" %% "play2-memcached" % "0.6.0"
)     


resolvers += "Objectify Play Repository" at "http://schaloner.github.com/releases/"

resolvers += "Objectify Play Snapshot Repository" at "http://schaloner.github.com/snapshots/"

resolvers += Resolver.sonatypeRepo("snapshots")

resolvers += "Sedis repository" at "http://pk11-scratch.googlecode.com/svn/trunk/"

//resolvers += "Spy Repository" at "http://files.couchbase.com/maven2"

resolvers += "sbt-plugin-snapshots" at "http://repo.scala-sbt.org/scalasbt/sbt-plugin-snapshots/"

resolvers += "Spy Repository" at "http://files.couchbase.com/maven2"



// Allow debugging of unit tests
Keys.fork in Test := false
