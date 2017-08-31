package gdx.scala.demo

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.{ApplicationAdapter, Gdx}
import gdx.scala.demo.logic._
import gdx.scala.demo.view.Renderer

import scala.collection.mutable.ListBuffer

class GdxScalaFpGame extends ApplicationAdapter {
  private lazy val renderer:Renderer = new Renderer(new SpriteBatch)

  private var world: WorldState = WorldState()
  private var snake: SnakeState = SnakeState()
  private var fruit: FruitState = FruitState()
  private val events:ListBuffer[Event] = ListBuffer()

  private def acceptKeys():Unit = {
    val ipt = Gdx.input
    if (ipt.isKeyPressed(Keys.LEFT)) {
      events += SnakeChangesDirection(Left)
    } else if (ipt.isKeyPressed(Keys.RIGHT)) {
      events += SnakeChangesDirection(Right)
    } else if (ipt.isKeyPressed(Keys.UP)) {
      events += SnakeChangesDirection(Up)
    } else if (ipt.isKeyPressed(Keys.DOWN)) {
      events += SnakeChangesDirection(Down)
    }
  }

  private def judge() = {
    if (snake.path.head.equals(fruit.position)) {
      events += SnakeBitesFruit()
    }
  }

  private def consumeEvents(): Unit = {
    implicit val worldState: WorldState = world
    while (events.nonEmpty) {
      val ev = events.remove(0)
      snake = ev.applyToSnake(snake)
      fruit = ev.applyToFruit(fruit)
    }
  }

  override def render(): Unit = {
    events += SnakeMoves()
    acceptKeys()
    judge()
    consumeEvents()

    renderer.render(snake, fruit, world)
  }

  /**
    * resize the window
    */
  override def resize(width: Int, height: Int): Unit = {
    super.resize(width, height)
    world = world.copy(size = Size(width, height))
    fruit = fruit.copy(position = world.randomPoint())
  }

  /**
    * Initialize the game screen
    */
  override def create(): Unit = super.create()

  override def pause(): Unit = super.pause()

  override def resume(): Unit = super.resume()

  override def dispose(): Unit = super.dispose()
}




