package gdx.scala.demo.logic

import GameConstants._

trait State

case class WorldState(width:Float,
                      height:Float) extends State

case class SnakeState(direction:Direction,
                      nextDirection:Direction,
                      path:List[Point],
                      speed:Float,
                      size:Int) extends State {
  val pathStep:Int = if (speed == 0) 1 else GRID_WIDTH / speed.toInt
  val top:Point = path.head
}
object SnakeState {
  def apply(direction: Direction = Stop,
            nextDirection: Direction = Stop,
            path: List[Point] = List(Point(0, 0)),
            speed: Float = 0,
            size: Int = 1): SnakeState =
    new SnakeState(direction, nextDirection, path, speed, size)
}

case class FruitState(position:Point) extends State
