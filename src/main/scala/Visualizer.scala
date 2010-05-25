import scala.swing.{ MainFrame, Panel, Dimension, Graphics2D }

class Visualizer (tastCase : TestCase) extends MainFrame {
  title = "Modrian Clustering Visualizer"

  contents = new Panel {
    val colors = {
      import java.awt.Color._
      List (red, green, blue, cyan, magenta, yellow, orange, pink)
    }

    override def paint (g: Graphics2D) = {
      g.setBackground (java.awt.Color.black)
      g.clearRect(0,0,size.width, size.height)

      for (rectSample <- tastCase.points) {
        val x = rectSample.sample.x * size.width.toDouble toInt
        val y = rectSample.sample.y * size.height.toDouble toInt

        g.setColor (colors (rectSample.rect.id % colors.size))
        g.fillRect (x, y, 2, 2)
      }
    }
  }

  size = new Dimension (700, 700)

  visible = true
}
