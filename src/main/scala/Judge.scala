class Permutation (perm: Array[Int]) {
  def apply[T] (seq: Seq[T]) = {
    assert (seq.length == perm.length)
    (i : Int) => seq (perm (i))
  }
}


object Permutation {
  def allMap[T] (n : Int) (f : (Permutation) => T) (implicit m: Manifest[T]) : Array[T] = {
    assert (n == 3)
    Array (f (new Permutation (Array (0, 1, 2))),
           f (new Permutation (Array (0, 2, 1))),
           f (new Permutation (Array (1, 0, 2))),
           f (new Permutation (Array (1, 2, 0))),
           f (new Permutation (Array (2, 0, 1))),
           f (new Permutation (Array (2, 1, 0)))
          )
  }
}


class Judge (testCase: TestCase, process: Process) {

  private case class AnswerPoint (rectSample : testCase.RectSample,
                                  baseBelief : Array[Double])

  private val answers = testCase.points map askOne

  val score : Double = Permutation.allMap (testCase.rectCount) (scoreOfPerm) max

  private def askOne (rectSample: testCase.RectSample) : AnswerPoint = {
    process.write (rectSample.sample.x toString)
    process.write (" ")
    process.write (rectSample.sample.y toString)
    process.write ("\n")
    val belief = process.readLine split (" ") map (_ toDouble)
    val totalBelief = belief sum;
    AnswerPoint (rectSample, belief map (_ / totalBelief))
  }

  def scoreOfPerm (perm: Permutation) : Double = {
    def apToScore (ap : AnswerPoint) = {
      val belief = perm (ap.baseBelief)
      val logLikelihood = math.log (belief (ap.rectSample.rect.id))
      logLikelihood / math.log (testCase.rectCount) + 1
    }
    answers map apToScore sum
  }
}
