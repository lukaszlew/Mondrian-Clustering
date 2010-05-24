import sbt._

class MondrianClusteringProject (info: ProjectInfo)
  extends DefaultProject (info) with PackageStaticAction
{
  val scalaSwing = "org.scala-lang" % "scala-swing" % "2.8.0.RC2"
}
