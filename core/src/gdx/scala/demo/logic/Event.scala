package gdx.scala.demo.logic

import GameSystem._

trait Event {
  def applyToSnake(snake:SnakeState)(implicit world:WorldState):SnakeState = snake
  def applyToFruit(fruit:FruitState)(implicit world:WorldState):FruitState = fruit
  def applyToWorld(world:WorldState):WorldState = world
}

case class SnakeChangesDirection(dir:Direction) extends Event {
  override def applyToSnake(snake:SnakeState)(implicit world:WorldState):SnakeState = {
    snake.copy(nextDirection = dir)
  }
}

case class SnakeBitesFruit() extends Event {
  override def applyToSnake(snake:SnakeState)(implicit world:WorldState):SnakeState = {
    val ap = snake.path.head
    snake.copy(size = snake.size + GameSystem.GROWTH_SIZE,
      path = snake.path.enqueue(List(ap, ap, ap)))
  }

  override def applyToFruit(fruit: FruitState)(implicit world:WorldState): FruitState = {
    fruit.copy(position = world.randomPoint())
  }
}

case class SnakeMoves() extends Event {
  override def applyToSnake(snake:SnakeState)(implicit world:WorldState):SnakeState = {
    val nextTop = snake.path.head.move(snake.direction, snake.speed)
    val nextPath = snake.path.enqueue(nextTop).dequeue._2
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
