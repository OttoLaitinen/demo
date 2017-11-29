package stardemo

import scala.swing._

object StarDemo extends SimpleSwingApplication {
  
  val WIDTH = 1280
  val HEIGHT = 720
  
  val starfield = new StarField(WIDTH, HEIGHT)
  
  def top = new MainFrame {
    contents = starfield
  }
  
  override def main(args: Array[String]) = {
    super.main(args)
    
    while (true) {
      starfield.tick()
      starfield.render()
      Thread.sleep(5)
    }
  }
  
}
