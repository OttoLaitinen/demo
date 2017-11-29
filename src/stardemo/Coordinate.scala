package stardemo

class Coordinate(var x: Double, var y: Double, var z: Double) {
  
  def op(o: Coordinate, op: (Double, Double) => Double) = {
    new Coordinate(op(x, o.x), op(y, o.x), op(z, o.z))
  }
  
  def +(o: Coordinate) = op(o, _ + _)
  
  def -(o: Coordinate) = op(o, _ - _)
  
  def *(o: Coordinate) = op(o, _ * _)
  
  def /(o: Coordinate) = op(o, _ / _)
  
  override def toString = f"($x, $y, $z)"
}