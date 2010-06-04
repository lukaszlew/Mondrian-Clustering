object Main {
  def usage () {
    println ("Usage: ./judge OPTIONS")
    println ()
    println ("OPTIONS:")
    println ("  -p PROGRAM      -- program to test (default none)")
    println ("  -vis            -- enables visualization (default off)")
    println ("  -seed SEED      -- sets starting seed for test series (default 0)")
    println ("  -n N            -- sets number of consecutive seeds to test (default 1) ")
    println ("  -o CSVFILE      -- sets output file (default result.csv)")
    println ("  -l LOGIN        -- your login (default ForgotToSetLogin)")
    println ()
  }

  def main (args : Array[String]) {
    if (args.size < 1) {
      usage ()
      return 1
    }

    var program : Option[String] = None
    var visualize = false
    var seedFrom = 0L
    var seedCount = 1
    var csvFile = ""
    var login = "ForgotToSetLogin"

    var i = 0
    while (i < args.size) {
      args (i) match {
        case "-p" =>
	  i += 1
	  program = Some (args(i))
	case "-vis" =>
	  visualize = true
	case "-seed" =>
	  i += 1
	  seedFrom = args(i) toLong
	case "-n" =>
	  i += 1
	  seedCount = args(i) toInt
	case "-o" =>
	  i += 1
	  csvFile = args(i)
	case "-l" => 
	  i += 1
	  login = args(i)
	case opt =>
	  println ("Unknown option: %s" format opt)
	  usage ()
	  return 1
      }
      i += 1
    }

    val score = new Array[Double] (seedCount toInt)
    var totalScore = 0.0
    for (i <- 0 until seedCount) {
      val seed = seedFrom + i
      val testCase = new TestCase (seed)

      program match {
        case Some (cmd) =>
	  val process = new Process (cmd)
	  val judge = new Judge (testCase, process)
      	  process.close ()
          println ("%d %f" format (seed, judge.score))
	  score (i) = judge.score
          totalScore += judge.score
	case None => ()
      }

      if (visualize) {
        new Visualizer (testCase)
        print ("Points per rectangle: ")
        println (testCase.pointsPerRect mkString " ")
      }
    }

    println ("Average score = %f" format (totalScore / seedCount))

    if (csvFile == "") csvFile = "results-" + login + ".csv"
    var csv = new java.io.PrintStream (new java.io.FileOutputStream (csvFile))
    csv.println ("# %s" format (args mkString " "))
    csv.println ((seedFrom until (seedFrom + seedCount) map ("seed%d" format _) mkString ", ") + ", login")
    csv.println ((score mkString ", ") + ", " + login)
    csv.close ()
  }
}
