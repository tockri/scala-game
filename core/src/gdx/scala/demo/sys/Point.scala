package gdx.scala.demo.sys

case class Point(x:Float, y:Float)

case class Size(width:Float, height:Float)

case class Rect(point:Point, size:Size) {
  val left:Float = point.x
  val top:Float = point.y
  val right:Float = left + size.width
  val bottom:Float = top + size.height
}


