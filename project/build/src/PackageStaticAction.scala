trait PackageStaticAction extends sbt.BasicScalaProject {
  lazy val packageStatic = {
    import sbt._

    val pathFinder = Path.lazyPathFinder {
      val tmp = outputPath / "package-static-tmp"

      val (libs, dirs) = runClasspath.get.toList.partition (ClasspathUtilities.isArchive)

      for (jar <- mainDependencies.scalaJars.get ++ libs)
        FileUtilities.unzip (jar, tmp, log).left.foreach (error)

      val base = Path.lazyPathFinder(tmp :: dirs) ##

      (descendents (base, "*") --- (base / "META-INF" ** "*")).get
    }

    val task = packageTask (pathFinder,
                            outputPath / (artifactID + "-static-" + version + ".jar"),
                            packageOptions)

    val description = "Creates a stand-alone jar file containing main classes, resources and all libraries."

    task dependsOn (compile) describedAs (description)
  }
}
