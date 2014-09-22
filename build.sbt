import sbtrelease._
import ReleaseStateTransformations._

releaseSettings

sonatypeSettings

organization := "com.gu"

scalaVersion := "2.10.4"

name := "flexible-content-block-to-text"

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

libraryDependencies ++= Seq(
  "org.jsoup" % "jsoup" % "1.7.3",
  "org.scalatest" % "scalatest_2.10" % "2.1.0" % "test"
)

description := "Flexible content block to text"

scmInfo := Some(ScmInfo(
  url("https://github.com/guardian/flexible-content-block-to-text"),
  "scm:git:git@github.com:guardian/flexible-content-block-to-text.git"
))

pomExtra := (
  <url>https://github.com/guardian/flexible-content-block-to-text</url>
    <developers>
      <developer>
        <id>robertberry</id>
        <name>Robert Berry</name>
        <url>https://github.com/robertberry</url>
      </developer>
    </developers>
  )

licenses := Seq("Apache V2" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))

ReleaseKeys.crossBuild := true

ReleaseKeys.releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(
    action = state => Project.extract(state).runTask(PgpKeys.publishSigned, state)._1,
    enableCrossBuild = true
  ),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(state => Project.extract(state).runTask(SonatypeKeys.sonatypeReleaseAll, state)._1),
  pushChanges
)
