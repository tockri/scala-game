package gdx.scala.demo

import com.badlogic.gdx.{ApplicationAdapter, Gdx, Input}
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.graphics.{Color, GL20, Pixmap, Texture}
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import gdx.scala.demo.GameConstants.{GRID_WIDTH, Stop}
// import com.badlogic.gdx.math.{Rectangle, Vector2}
import sys._

import scala.collection.mutable.ListBuffer

object GameConstants {
  val GRID_WIDTH = 15
  val SNAKE_COLOR = new Color(0.8f, 0.8f, 0.2f, 1)
  val FRUIT_COLOR = new Color(0.4f, 0.8f, 0.8f, 1)
  val GROWTH_SIZE = 3
  sealed abstract class Direction(val x:Float, val y:Float)
  object Stop extends Direction(0, 0)
  object Left extends Direction(-1, 0)
  object Right extends Direction(1, 0)
  object Up extends Direction(0, 1)
  object Down extends Direction(0, -1)
}


case class WorldState(width:Float,
                      height:Float)

case class SnakeState(direction:GameConstants.Direction,
                      nextDirection:GameConstants.Direction,
                      path:List[Point],
                      speed:Float,
                      size:Int) {
  import GameConstants._
  val pathStep:Int = GRID_WIDTH / speed.toInt

}
object SnakeState {
  def apply(direction: GameConstants.Direction = Stop,
            nextDirection: GameConstants.Direction = Stop,
            path: List[Point] = List(Point(0, 0)),
            speed: Float = 0,
            size: Int = 1): SnakeState =
    new SnakeState(direction, nextDirection, path, speed, size)
}

case class FruitState(position:Point)

trait Event

trait SnakeEvent extends Event {
  def applyTo(snake:SnakeState)(implicit world:WorldState):SnakeState
}

trait FruitEvent extends Event {
  def applyTo(fruit:FruitState)(implicit world:WorldState):FruitState
}

case class ChangeDirection(dir:GameConstants.Direction) extends SnakeEvent {
  def applyTo(snake:SnakeState)(implicit world:WorldState):SnakeState = {
    snake.copy(nextDirection = dir)
  }
}

case class BiteFruit() extends SnakeEvent with FruitEvent {
  def applyTo(snake:SnakeState)(implicit world:WorldState):SnakeState = {
    snake.copy(size = snake.size + GameConstants.GROWTH_SIZE)
  }

  override def applyTo(fruit: FruitState)(implicit world:WorldState): FruitState = {
    fruit.copy(position = Point(
      GRID_WIDTH * Math.floor(Math.random() * world.width / GRID_WIDTH).toFloat,
      GRID_WIDTH * Math.floor(Math.random() * world.height / GRID_WIDTH).toFloat))
  }
}

case class EveryFrame() extends SnakeEvent {
  import GameConstants._
  def applyTo(snake:SnakeState)(implicit world:WorldState):SnakeState = {
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

object Textures {
  import GameConstants._
  lazy val snake = {
    val pm = new Pixmap(GRID_WIDTH, GRID_WIDTH, Format.RGBA8888)
    pm.setColor(SNAKE_COLOR)
    pm.fillRectangle(0, 0, GRID_WIDTH, GRID_WIDTH)
    val t = new Texture(pm)
    pm.dispose()
    t
  }

  lazy val fruit = {
    val pm = new Pixmap(GRID_WIDTH, GRID_WIDTH, Format.RGBA8888)
    pm.setColor(FRUIT_COLOR)
    pm.fillRectangle(0, 0, GRID_WIDTH, GRID_WIDTH)
    val t = new Texture(pm)
    pm.dispose()
    t
  }
}


class GdxScalaFpGame extends ApplicationAdapter {
  import GameConstants._
  private var batch: SpriteBatch = _

  private implicit var world: WorldState = WorldState(0, 0)
  private var snake: SnakeState = SnakeState(direction = Stop,
    nextDirection = Stop,
    path = List(Point(0, 0)),
    speed = 0,
    size = 1)
  private var fruit: FruitState = FruitState(Point(0, 0))
  private val events:ListBuffer[Event] = ListBuffer()

  /**
    * Initialize the game screen
    */
  override def create() = {
    batch = new SpriteBatch

  }

  private def processKeyEvent():Unit = {
    val ipt = Gdx.input
    if (ipt.isKeyPressed(Keys.LEFT)) {
      events += ChangeDirection(Left)
    } else if (ipt.isKeyPressed(Keys.RIGHT)) {
      events += ChangeDirection(Right)
    } else if (ipt.isKeyPressed(Keys.UP)) {
      events += ChangeDirection(Up)
    } else if (ipt.isKeyPressed(Keys.DOWN)) {
      events += ChangeDirection(Down)
    }
  }


  private def judge() = {
    val top = snakePath.head
    if (top.equals(fruitPos)) {
      snakeSize += GROWTH_SIZE
      putFruit()
    }
  }

  private def renderSnake() = {
    val step = pathStep
    for (i <- 0 until snakePath.length if i % step == 0) {
      val pos = snakePath(i)
      batch.draw(Textures.snake, pos.x, pos.y)
    }
  }

  private def renderFruit() = {
    batch.draw(Textures.fruit, fruitPos.x, fruitPos.y)
  }

  override def render() = {
    processKeyEvent()
    updatePosition()
    judge()

    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.begin()
    renderSnake()
    renderFruit()
    batch.end()
  }

  /**
    * resize the window
    */
  override def resize(width: Int, height: Int): Unit = {
    super.resize(width, height)
    world = world.copy(width = width, height = height)
  }

  override def pause(): Unit = super.pause()

  override def resume(): Unit = super.resume()

  override def dispose(): Unit = super.dispose()
}




