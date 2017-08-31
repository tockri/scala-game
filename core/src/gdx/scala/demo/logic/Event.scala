package gdx.scala.demo.logic

import GameSystem._

trait Event {
  def applyToSnake(snake:SnakeState)(implicit world:WorldState):SnakeState = snake
  def applyToFruit(fruit:FruitState)(implicit world:WorldState):FruitState = fruit
}

case class SnakeChangesDirection(dir:Direction) extends Event {
  override def applyToSnake(snake:SnakeState)(implicit world:WorldState):SnakeState = {
    snake.copy(nextDirection = dir)
  }
}

case class SnakeBitesFruit() extends Event {
  override def applyToSnake(snake:SnakeState)(implicit world:WorldState):SnakeState = {
    val ap = snake.path.head
    val nextSize = snake.size + GameSystem.GROWTH_SIZE
    val pathLength = nextSize * (GRID_WIDTH / snake.speed).toInt
    val nextPath = (snake.path.length until pathLength).map(_ => ap).toList ++ snake.path
    snake.copy(size = nextSize, path = nextPath)
  }

  override def applyToFruit(fruit: FruitState)(implicit world:WorldState): FruitState = {
    fruit.copy(position = world.randomPoint())
  }
}

case class SnakeMoves() extends Event {
  override def applyToSnake(snake:SnakeState)(implicit world:WorldState):SnakeState = {
    val nextTop = snake.path.last.move(snake.direction, snake.speed)
    val nextPath = snake.path.tail :+ nextTop
    val direction = if (isOnGrid(nextTop)) {
      snake.nextDirection
    } else {
      snake.direction
    }
    snake.copy(
      direction = direction,
      path = nextPath
    )
  }
}
