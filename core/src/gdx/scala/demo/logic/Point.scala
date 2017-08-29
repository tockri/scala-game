package gdx.scala.demo.logic

case class Point(x:Float, y:Float) {
  def move(direction: Direction, speed:Float):Point =
    Point(x + direction.x * speed, y + direction.y * speed)
}

case class Size(width:Float, height:Float)

case class Rect(point:Point, size:Size) {
  val left:Float = point.x
  val top:Float = point.y
  val right:Float = left + size.width
  val bottom:Float = top + size.height
}

object Rect {
  def apply(left:Float, top:Float, width:Float, height:Float): Rect =
    new Rect(Point(left, top), Size(width, height))
}
