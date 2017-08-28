package gdx.scala.demo

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.{ApplicationAdapter, Gdx}
import gdx.scala.demo.logic._
import gdx.scala.demo.view.Renderer

import scala.collection.mutable.ListBuffer

class GdxScalaFpGame extends ApplicationAdapter {
  private lazy val renderer:Renderer = new Renderer(new SpriteBatch)

  private var world: WorldState = WorldState(0, 0)
  private var snake: SnakeState = SnakeState(direction = Stop,
    nextDirection = Stop,
    path = List(Point(0, 0)),
    speed = 3,
    size = 1)
  private var fruit: FruitState = FruitState(Point(0, 0))
  private val events:ListBuffer[Event] = ListBuffer()

  private def acceptKeys():Unit = {
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
    if (snake.top.equals(fruit.position)) {
      events += BiteFruit()
    }
  }

  private def consumeEvents() = {
    implicit val worldState = world
    while (events.nonEmpty) {
      events.remove(0) match {
        case ev:CombinedEvent =>
          snake = ev.applyTo(snake)
          fruit = ev.applyTo(fruit)
        case ev:SnakeEvent => snake = ev.applyTo(snake)
        case ev:FruitEvent => fruit = ev.applyTo(fruit)
      }
    }
  }

  override def render(): Unit = {
    events += EveryFrame()
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
    world = world.copy(width = width, height = height)
  }

  /**
    * Initialize the game screen
    */
  override def create(): Unit = super.create()

  override def pause(): Unit = super.pause()

  override def resume(): Unit = super.resume()

  override def dispose(): Unit = super.dispose()
}




