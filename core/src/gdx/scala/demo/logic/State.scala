package gdx.scala.demo.logic

import GameSystem._

import scala.collection.immutable.Queue

trait State

case class WorldState(size: Size = Size(0, 0)) extends State {
  def randomPoint():Point =
    Point(
      GRID_WIDTH * Math.floor(Math.random() * size.width / GRID_WIDTH).toFloat,
      GRID_WIDTH * Math.floor(Math.random() * size.height / GRID_WIDTH).toFloat)
}

case class SnakeState(direction:Direction = Stop,
                      nextDirection:Direction = Stop,
                      path:List[Point] = List(Point(0, 0)),
                      speed:Float = 1.5F,
                      size:Int = 1) extends State

case class FruitState(position:Point = Point(0, 0)) extends State
