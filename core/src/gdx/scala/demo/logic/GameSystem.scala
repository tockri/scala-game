package gdx.scala.demo.logic

import com.badlogic.gdx.graphics.Color
import gdx.scala.demo.geometry.Point


object GameSystem {
  val GRID_WIDTH = 15
  val SNAKE_COLOR = new Color(0.8f, 0.8f, 0.2f, 1)
  val FRUIT_COLOR = new Color(0.4f, 0.8f, 0.8f, 1)
  val GROWTH_SIZE = 3

  def isOnGrid(point:Point):Boolean =
    point.x.toInt % GRID_WIDTH == 0 && point.y.toInt % GRID_WIDTH == 0
}
