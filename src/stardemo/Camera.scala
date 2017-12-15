package stardemo

object Camera {
  
  val pos = new Coordinate(0.0, 0.0, 0.0)
  
  def pointOnViewport(point: Coordinate, v: Viewport): Option[Coordinate] = {
    val f = v.z / point.z
    val rx = f * point.x
    val ry = f * point.y
    
    val rpoint = new Coordinate(rx, ry, v.z) 
    
    if (v.pointOutOfBounds(rpoint)) None
    else Some(v.canvasPoint(rpoint))
  }
  
}


class Viewport(var distance: Double, val width: Double = StarDemo.WIDTH, val height: Double = StarDemo.HEIGHT) {
  
  def pos = new Coordinate(0.0, 0.0, distance)
  
  def x = pos.x
  def y = pos.y
  def z = pos.z
  
  def pointOutOfBounds(point: Coordinate) = point.x > (pos.x + width)  || 
                                            point.x < (pos.x - width)  ||
                                            point.y > (pos.y + height) ||
                                            point.y < (pos.y - height)
  
  def canvasPoint(point: Coordinate) = new Coordinate(width/2 + point.x, height/2 + point.y, point.z)
  
}