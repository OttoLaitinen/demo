package stardemo

import scala.util.Random
import scala.math.round
import scala.collection.mutable.ArrayBuffer
import scala.swing._
import java.awt.{Color}
import java.awt.image.BufferedImage


class Cube2(val cubeSize: Int, val width: Int, val height: Int) extends BoxPanel(Orientation.Horizontal) with Animation { 
  



  
  val content = new Panel {
    preferredSize = new Dimension(width, height)
    
    override def paintComponent(g: Graphics2D) = {
      g.clearRect(0, 0, size.width, size.height)
      
      g.setColor(Color.RED)
      g.fillRect(0, 0, size.width, size.height)

      
    }
  }
  
  //contents += controls
  contents += content
  
  val viewport = new Viewport(150)
  
  val stars = (new Star(new Coordinate(1.0, 1.0, 151.0)))


  
  var time = 0
  
  val camera = Camera
    
  def tick() = {
    time += 1
    
  }
  
  def render() = {
    content.revalidate()
    content.repaint()
  }
  
}


