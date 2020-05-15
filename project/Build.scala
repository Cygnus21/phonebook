import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "phonebook"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    jdbc,
    slick,
    "postgresql" % "postgresql" % "8.4-702.jdbc4"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    lessEntryPoints <<= baseDirectory(_ / "app" / "assets" / "stylesheets" ** "bootstrap.less")
  )
}