import java.security.SecureRandom

class TestCase (seedInit : Long) {
  case class Point (x: Double, y: Double)
  case class Rect (id: Int, origin: Point, dim: Point, gamma: Double)
  case class RectSample (rect: Rect, sample: Point)

  val random = SecureRandom.getInstance ("SHA1PRNG")
  val seed = seedInit
  random.setSeed (seed)

  val rectCount  = 3
  val pointCount = 1000

  val rects = List.tabulate[Rect] (rectCount) (nextRect _)
  val totalGamma = rects map (_.gamma) sum
  val points = List.tabulate[RectSample] (pointCount) (nextRectSample)
  val pointsPerRect = Array.tabulate[Int] (rectCount) ((id: Int) => points count (_.rect.id == id))

  def nextRect (id : Int): Rect = {
    val dx = random.nextDouble ()
    val dy = random.nextDouble ()
    val x = random.nextDouble () * (1.0 - dx)
    val y = random.nextDouble () * (1.0 - dy)
    new Rect (id, Point (x, y), Point (dx, dy), random.nextDouble ())
  }
  
  def nextRectSample (i : Int) : RectSample = {
    val s = random.nextDouble () * totalGamma
    var sum = 0.0
    for (rect <- rects) {
      sum += rect.gamma
      if (sum >= s) {
        val sample = 
          Point (rect.origin.x + random.nextDouble () * rect.dim.x,
                 rect.origin.y + random.nextDouble () * rect.dim.y)
        return RectSample (rect, sample)
      }
    }
    throw new AssertionError
  }
}
