package stardemo

import scala.util.Random
import scala.math._
import scala.collection.mutable.ArrayBuffer
import scala.swing._
import java.awt.{ Color }
import java.awt.image.BufferedImage
import java.awt.Graphics

import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

class StarField(val width: Int, val height: Int) extends BoxPanel(Orientation.Horizontal) with Animation {
  private var offSet = 0.0
  private var spinDirection = true
  val viewportSlider = new Slider {
    min = 1
    max = 200
    majorTickSpacing = 50
    minorTickSpacing = 5
    //paintTicks = true
    //paintLabels = true
  }

  val spawnzSlider = new Slider {
    min = -3000
    max = 0
  }

  val radiusSlider = new Slider {
    min = 2
    max = 10
  }

  val spawnsizeSlider = new Slider {
    min = 100
    max = 100000
  }

  val maxstarsSlider = new Slider {
    min = 1
    max = 100
  }

  val controls = new BoxPanel(Orientation.Vertical) {
    contents += new Label("Viewport distance")
    contents += viewportSlider

    contents += new Label("Spawn Z")
    contents += spawnzSlider

    contents += new Label("Radius")
    contents += radiusSlider

    contents += new Label("Spawn size")
    contents += spawnsizeSlider

    contents += new Label("Max stars")
    contents += maxstarsSlider
  }
  var milleniumFalcon: BufferedImage = _
  milleniumFalcon = ImageIO.read(new File("img/millenniumrenderback.png"))

  val content = new Panel {
    preferredSize = new Dimension(width, height)

    override def paintComponent(g: Graphics2D) = {
      g.clearRect(0, 0, size.width, size.height)
      g.setColor(Color.black)
      g.fillRect(0, 0, size.width, size.height)

      if (spinDirection) {
        offSet -= 1.0 / 750
      } else {
        offSet += 1.0 / 750
      }
      g.rotate(offSet, size.width / 2, size.height / 2)
      if (offSet > Pi / 6) {
        spinDirection = true
      } else if (offSet < -Pi / 6) {
        spinDirection = false
      }
      stars.foreach(drawStar(_, g))
      
      val wantedWidth = (milleniumFalcon.getWidth * (800.0 / time)).toInt
      val wantedHeight = (milleniumFalcon.getHeight * (800.0 / time)).toInt
      g.drawImage(
        milleniumFalcon,
        size.width / 2 - wantedWidth / 2,
        size.height / 2 - wantedHeight / 2,
        wantedWidth,
        wantedHeight,
        null)

    }
  }

  //contents += controls
  contents += content

  val viewport = new Viewport(150)
  var spawnz = -200
  var radius = 2
  var spawnSize = 1000
  var maxStars = 15

  viewportSlider.value = viewport.distance.toInt
  spawnzSlider.value = spawnz
  radiusSlider.value = radius
  spawnsizeSlider.value = spawnSize
  maxstarsSlider.value = maxStars

  def rngDouble = {
    Random.nextDouble() * spawnSize - spawnSize / 2
  }

  var time = 0

  var stars = ArrayBuffer[Star]()
  val camera = Camera

  def newStar: Star = {
    new Star(new Coordinate(rngDouble, rngDouble, spawnz))
  }

  def drawStar(s: Star, g: Graphics2D) = {
    val canvasPos = camera.pointOnViewport(s.pos, viewport)

    canvasPos.foreach(point => {

      val number = abs(s.pos.z.toInt) / 10
      val oldCanvasPos = camera.pointOnViewport(new Coordinate(s.pos.x, s.pos.y, s.pos.z - number), viewport)
      oldCanvasPos.foreach(oldPoint => {
        g.setColor(s.color)
        g.drawLine(point.x.toInt, point.y.toInt, oldPoint.x.toInt, oldPoint.y.toInt)

      })
    })

  }

  def tick() = {
    time += 1
    viewport.distance = viewportSlider.value
    spawnz = spawnzSlider.value
    radius = radiusSlider.value
    spawnSize = spawnsizeSlider.value
    maxStars = maxstarsSlider.value

    for (x <- 1 to maxStars) {
      stars += newStar
    }

    stars.foreach(_.pos.z += 1)
    stars = stars.filter(s => s.pos.z < 0 /* && camera.pointOnViewport(s.pos, viewport).isDefined*/ )
    
  }

  def render() = {
    content.repaint()
  }

  private def playMusic() {
      val audioInputStream = AudioSystem.getAudioInputStream(new File("img/Star Wars Millenium Falcon Theme.wav").getAbsoluteFile());
      val clip = AudioSystem.getClip();
      clip.open(audioInputStream);
      clip.start();

  }
  playMusic()

}

class Star(var pos: Coordinate, var color: Color = Color.CYAN) {

}