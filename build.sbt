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
"com.github.mumoshu" %% "play2-memcached" % "0.6.0",
  "redis.clients" % "jedis" % "2.6.2"
)     


resolvers ++= Seq(
Resolver.sonatypeRepo("snapshots"),
Resolver.sonatypeRepo("master"),
"Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
"Online Play Repository" at "http://repo.typesafe.com/typesafe/simple/maven-releases/",
"Spy Repository" at "http://files.couchbase.com/maven2",
"Morphia Repo" at "http://morphia.googlecode.com/svn/mavenrepo/" )



// Allow debugging of unit tests
Keys.fork in Test := false
