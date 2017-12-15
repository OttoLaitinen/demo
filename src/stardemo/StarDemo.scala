package stardemo

import scala.swing._

object StarDemo extends SimpleSwingApplication {

  val WIDTH = 1920
  val HEIGHT = 1080

  val starfield = new StarField(WIDTH, HEIGHT)
  val superCube = new Cube2(10, WIDTH, HEIGHT)

  def top = new MainFrame {
    contents = starfield
  }

  override def main(args: Array[String]) = {
    super.main(args)

    while (true) {
      //if (starfield.time < 1000) {
        starfield.tick()
        starfield.render()
        Thread.sleep(5)
//     // } else {
//        superCube.tick()
//        superCube.render()
//        Thread.sleep(5)
//        println("here")
//      }

    }
  }

}
