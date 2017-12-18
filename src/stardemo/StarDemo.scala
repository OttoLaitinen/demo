package stardemo

import scala.swing._
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

object StarDemo extends SimpleSwingApplication {
  
  /*TURN YOUR VOLUME UP
   * and then run this program...*/

  val WIDTH = 1920
  val HEIGHT = 1080

  val starfield = new StarField(WIDTH, HEIGHT)

  def top = new MainFrame {
    contents = starfield 
    
  }

  override def main(args: Array[String]) = {
    super.main(args)

    while (true) {
      if (starfield.time < 17500) {
        starfield.tick()
        starfield.render()
        Thread.sleep(5)
      } else {
        quit()
      }

    }
  }

}
