import java.security.SecureRandom

// -----------------------------------------------------------------------------

case class Point (x: Double, y: Double)

class Rect (i: Int, p: Point, d: Point, g: Double) {
  val id = i
  val gamma = g

  def sample (rand: SecureRandom): Point = {
    Point(x = p.x + rand.nextDouble () * d.x,
          y = p.y + rand.nextDouble () * d.y)
  }
}

// -----------------------------------------------------------------------------

class TestCase (seed: Long) {
  import scala.collection.mutable.Stack

  val maxRect = 10
  val maxPoints = 10000

  val random = SecureRandom.getInstance ("SHA1PRNG")
  random.setSeed (seed)

  val rects = Stack[Rect] ()
  
  def newRect (i : Int): Rect = {
    val dx = random.nextDouble ()
    val dy = random.nextDouble ()
    val x = random.nextDouble () * (1.0 - dx)
    val y = random.nextDouble () * (1.0 - dy)
    new Rect (i, Point (x, y), Point (dx, dy), random.nextDouble ())
  }

  for (i <- 0 until random.nextInt (maxRect))
    rects push newRect (i)

  val totalGamma = rects map (_.gamma) sum
  
  def sampleRect () : Rect = {
    val s = random.nextDouble () * totalGamma
    var sum = 0.0
    for (rect <- rects) {
      sum += rect.gamma
      if (sum >= s) return rect
    }
    throw new AssertionError
  }

  case class RectPoint (pt: Point, rect: Rect)

  def sample () = {
    val rect = sampleRect()
    RectPoint (rect.sample(random), rect)
  }

  val points = List.tabulate[RectPoint] (random nextInt maxPoints) ((_: Int) => sample())

  def saveToScv (fileName: String) {
    val file = ()
    for (point <- points) {
      //file . write ...
    }
    // file. close
  }
}
