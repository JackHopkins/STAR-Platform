name := "template"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.5"

doc in Compile <<= target.map(_ / "none")

//maintainer:= "Jack Hopkins"
 
// exposing the play ports
//dockerExposedPorts in Docker := Seq(9000)


libraryDependencies ++= Seq(
  javaCore,
  javaJdbc,
  javaEbean,
  javaWs,
  cache,
  "com.newrelic.agent.java" % "newrelic-api" % "3.1.1",
  "com.google.guava" % "guava" % "14.0",
  "ws.securesocial" %% "securesocial" % "master-SNAPSHOT",
  "se.digiplant" %% "play-res" % "1.1.1",
  "com.github.mumoshu" %% "play2-memcached" % "0.6.0",
  "redis.clients" % "jedis" % "2.6.2",
  "io.keen" % "keen-client-api-java" % "2.1.1",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.5.1",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.5.1",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.5.1",
  "org.json" % "json" % "20140107",
  "org.apache.commons" % "commons-collections4" % "4.0",
  "org.apache.commons" % "commons-email" % "1.3.3",
  "org.apache.httpcomponents" % "httpclient" % "4.4",
  "org.apache.httpcomponents" % "fluent-hc" % "4.4",
  "org.apache.httpcomponents" % "httpmime" % "4.4",
  "com.googlecode.json-simple" % "json-simple" % "1.1.1",
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "com.mandrillapp.wrapper.lutung" % "lutung" % "0.0.4",
  "org.apache.commons" % "commons-email" % "1.1.3",
  "org.mongodb.morphia" % "morphia" % "0.107",
  "org.mongodb" % "mongo-java-driver" % "2.12.2",
  "org.mongodb" % "bson" % "2.13.0",
  "com.esotericsoftware.yamlbeans" % "yamlbeans" % "1.08",
   "org.rythmengine" % "rythm-engine" % "1.1.1-SNAPSHOT",
   "net.sf.opencsv" % "opencsv" % "2.3",
    "it.unibo.alice.tuprolog" % "tuprolog" % "2.1.1",
    "org.xerial" % "sqlite-jdbc" % "3.8.10.1",
    "org.avaje.ebeanorm" % "avaje-ebeanorm-agent" % "4.5.3",
    "org.avaje" % "avaje-agentloader" % "1.1.2"
)     

TwirlKeys.templateImports += "se.digiplant._"

resolvers ++= Seq(
Resolver.sonatypeRepo("snapshots"),
Resolver.sonatypeRepo("master"),
"Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
"Online Play Repository" at "http://repo.typesafe.com/typesafe/simple/maven-releases/",
"Spy Repository" at "http://files.couchbase.com/maven2",
"Morphia Repo" at "http://morphia.googlecode.com/svn/mavenrepo/" )



// Allow debugging of unit tests
Keys.fork in Test := false
