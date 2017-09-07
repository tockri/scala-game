package gdx.scala.demo.logic

import GameSystem._

trait Event {

}
trait SnakeEvent extends Event {
  def consume(snake:SnakeState, world:WorldContext):(SnakeState, Seq[Event])
}
trait FruitEvent extends Event {
  def consume(fruit:FruitState, world: WorldContext):(FruitState, Seq[Event])
}


case class SnakeChangesDirection(dir:Direction) extends SnakeEvent {
  override def consume(snake:SnakeState, world:WorldContext):(SnakeState, Seq[Event]) = {
    (snake.copy(nextDirection = dir), Nil)
  }
}

case class SnakeBitesFruit() extends SnakeEvent {
  override def consume(snake:SnakeState, world:WorldContext):(SnakeState, Seq[Event]) = {
    (snake, Seq(SnakeGrow(), PutFruitRandomPoint()))
  }
}

case class SnakeGrow() extends SnakeEvent {
  override def consume(snake:SnakeState, world:WorldContext):(SnakeState, Seq[Event]) = {
    val ap = snake.path.head
    val nextSize = snake.size + GameSystem.GROWTH_SIZE
    val pathLength = nextSize * (GRID_WIDTH / snake.speed).toInt
    val nextPath = (snake.path.length until pathLength).map(_ => ap).toList ++ snake.path
    (snake.copy(size = nextSize, path = nextPath), Nil)
  }
}

case class PutFruitRandomPoint() extends FruitEvent {
  override def consume(fruit:FruitState, world: WorldContext):(FruitState, Seq[Event]) = {
    (fruit.copy(position = world.randomPoint()), Nil)
  }
}

case class SnakeMoves() extends SnakeEvent {
  override def consume(snake:SnakeState, world:WorldContext):(SnakeState, Seq[Event]) = {
    val nextTop = snake.path.last.move(snake.direction, snake.speed)
    val nextPath = snake.path.tail :+ nextTop
    val direction = if (isOnGrid(nextTop)) {
      snake.nextDirection
    } else {
      snake.direction
    }
    (snake.copy(
      direction = direction,
      path = nextPath
    ), Nil)
  }
}
