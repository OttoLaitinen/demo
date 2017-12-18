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

  var time = 0

  var stars = ArrayBuffer[Star]()
  val camera = Camera

  val viewportSlider = new Slider {
    min = 1
    max = 200
    majorTickSpacing = 50
    minorTickSpacing = 5
    //paintTicks = true
    //paintLabels = true
  }

  /*Sliders for testing */
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

  /*Controls for testing purposes*/
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

    /*This paints all the compnents to the screen*/
    override def paintComponent(g: Graphics2D) = {
      /*Background*/
      g.clearRect(0, 0, size.width, size.height)
      g.setColor(Color.black)
      g.fillRect(0, 0, size.width, size.height)

      /*Some values*/
      val spinSpeed = 1.0 / 750
      val picScale = 800.0 / time
      val xSpeed = time / 750.0 
      /*Spinning effect*/
      if (spinDirection) {
        offSet -= spinSpeed
      } else {
        offSet += spinSpeed
      }
      g.rotate(offSet, size.width / 2, size.height / 2)
      if (offSet > Pi / 6) { //Spin limit
        spinDirection = true
      } else if (offSet < -Pi / 6) {
        spinDirection = false
      }

      /*Stars are drawed here*/
      stars.foreach(drawStar(_, g))

      /*Values for drawing the Falcon and making it move*/
      val wantedWidth = (milleniumFalcon.getWidth * (picScale)).toInt
      val wantedHeight = (milleniumFalcon.getHeight * (picScale)).toInt
      val xPosition = ((size.width / 2) + (wantedWidth / 2.0) * sin(xSpeed)).toInt - wantedWidth / 2
      val yPosition = size.height / 2 - wantedHeight / 2

      /*Drawing falcon*/
      g.drawImage(
        milleniumFalcon,
        xPosition,
        yPosition,
        wantedWidth,
        wantedHeight,
        null)

    }
  }

  //contents += controls //For testing
  contents += content

  /*Values*/
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

  /*Random that is used to generate stars inside a certain area*/
  def rngDouble = {
    Random.nextDouble() * spawnSize - spawnSize / 2
  }

  /*Creates a star inside the possible area*/
  def newStar: Star = {
    new Star(new Coordinate(rngDouble, rngDouble, spawnz))
  }

  def drawStar(s: Star, g: Graphics2D) = {
    val canvasPos = camera.pointOnViewport(s.pos, viewport) //orginal point on the screen

    canvasPos.foreach(point => {

      val number = abs(s.pos.z.toInt) / 10 //how long the line will be is defined by this number
      val oldCanvasPos = camera.pointOnViewport(new Coordinate(s.pos.x, s.pos.y, s.pos.z - number), viewport)
      oldCanvasPos.foreach(oldPoint => {
        g.setColor(s.color)
        g.drawLine(point.x.toInt, point.y.toInt, oldPoint.x.toInt, oldPoint.y.toInt)

      })
    })

  }

  def tick() = {
    time += 1 //Used for tick counting

    /*Sliders for testing*/
    viewport.distance = viewportSlider.value
    spawnz = spawnzSlider.value
    radius = radiusSlider.value
    spawnSize = spawnsizeSlider.value
    maxStars = maxstarsSlider.value

    /*New stars are created here*/
    for (x <- 1 to maxStars) {
      stars += newStar
    }

    /*Stars are moved closer to the camera here
     * after the stars are not visible they are deleted*/
    stars.foreach(_.pos.z += 1)
    stars = stars.filter(s => s.pos.z < 0 /* && camera.pointOnViewport(s.pos, viewport).isDefined*/ )

  }

  def render() = {
    content.repaint()
  }

  /*Made as a method for easier testing--
   * Plays music*/
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