package gdx.scala.demo.logic

import GameConstants._

trait Event

trait SnakeEvent extends Event {
  def applyTo(snake:SnakeState)(implicit world:WorldState):SnakeState
}

trait FruitEvent extends Event {
  def applyTo(fruit:FruitState)(implicit world:WorldState):FruitState
}

trait CombinedEvent extends SnakeEvent with FruitEvent

case class ChangeDirection(dir:Direction) extends SnakeEvent {
  def applyTo(snake:SnakeState)(implicit world:WorldState):SnakeState = {
    snake.copy(nextDirection = dir)
  }
}

case class BiteFruit() extends CombinedEvent {
  override def applyTo(snake:SnakeState)(implicit world:WorldState):SnakeState = {
    snake.copy(size = snake.size + GameConstants.GROWTH_SIZE)
  }

  override def applyTo(fruit: FruitState)(implicit world:WorldState): FruitState = {
    fruit.copy(position = Point(
      GRID_WIDTH * Math.floor(Math.random() * world.width / GRID_WIDTH).toFloat,
      GRID_WIDTH * Math.floor(Math.random() * world.height / GRID_WIDTH).toFloat))
  }
}

case class EveryFrame() extends SnakeEvent {
  override def applyTo(snake:SnakeState)(implicit world:WorldState):SnakeState = {
    val top = snake.path.head
    val nextTop = Point(top.x + snake.direction.x * snake.speed,
      top.y + snake.direction.y * snake.speed)
    var nextPath = nextTop :: snake.path
    val toRemove = snake.path.length - snake.size * snake.pathStep
    if (toRemove > 0) {
      nextPath = nextPath.slice(0, nextPath.length - toRemove)
    }
    val direction = if (nextTop.x.toInt % GRID_WIDTH == 0 && nextTop.y.toInt % GRID_WIDTH == 0) {
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
