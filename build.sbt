
organization := "com.gu"

name := "flexible-content-block-to-text"

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

libraryDependencies ++= Seq(
  "org.jsoup" % "jsoup" % "1.7.3",
  "org.scalatest" % "scalatest_2.10" % "2.1.0" % "test"
)
