object Main {
  def main (args : Array[String]) {
    if (args.size != 1) {
      println ("Error: no seed parameter")
      return 1
    }

    val testCase = new TestCase (args(0).toLong)
    val visualiser = new Visualizer (testCase)
  }
}
