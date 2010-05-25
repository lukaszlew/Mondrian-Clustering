object Main {
  def usage () {
    println ("Usage: ./judge PROGRAM SEED_START SEED_END VISUALIZE")
    println ("  VISUALIZE - either vis or novis")
  }

  def main (args : Array[String]) {
    if (args.size != 4) {
      usage ()
      return 1
    }

    val program = args (0)
    val seedStart = args (1) toLong
    val seedEnd = args (2) toLong
    val doVis = args (3) match {
      case "vis" => true
      case "novis" => false
      case _ => usage (); return 1
    }

    var totalScore = 0.0
    var n = 0.0
    for (seed <- seedStart to seedEnd) {
      val testCase = new TestCase (seed)
      val process = new Process (program)
      val judge = new Judge (testCase, process)
      process.close ()
      if (doVis) {
        new Visualizer (testCase)
        print ("Points per rectangle: ")
        println (testCase.pointsPerRect mkString " ")
      }
      println ("%d %f" format (seed, judge.score))
      totalScore += judge.score
      n += 1
    }
    println ("Average score = %f" format (totalScore / n))
  }
}
