package gdx.scala.demo

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.{ApplicationAdapter, Gdx, InputAdapter}
import gdx.scala.demo.logic._
import gdx.scala.demo.view.Renderer

class GdxScalaFpGame extends ApplicationAdapter {
  private lazy val renderer:Renderer = new Renderer(new SpriteBatch)

  private var world: WorldState = WorldState()
  private var snake: SnakeState = SnakeState()
  private var fruit: FruitState = FruitState()
  private var events:List[Event] = Nil

  private def addEvent(ev:Event):Unit = {
    synchronized {
      events = ev :: events
    }
  }

  private def judge(): Unit = {
    if (snake.path.last.equals(fruit.position)) {
      addEvent(SnakeBitesFruit())
    }
  }

  private def consumeEvents(): Unit = {
    implicit val worldState: WorldState = world
    val loop = synchronized {
      val loop = events.reverse
      events = Nil
      loop
    }
    loop.foreach {ev =>
      snake = ev.applyToSnake(snake)
      fruit = ev.applyToFruit(fruit)
    }
  }

  override def render(): Unit = {
    addEvent(SnakeMoves())
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
  override def create(): Unit = {
    Gdx.input.setInputProcessor(new InputAdapter() {
      override def keyDown(keyCode: Int): Boolean = {
        keyCode match {
          case Keys.LEFT => addEvent(SnakeChangesDirection(Left))
          case Keys.RIGHT => addEvent(SnakeChangesDirection(Right))
          case Keys.UP => addEvent(SnakeChangesDirection(Up))
          case Keys.DOWN => addEvent(SnakeChangesDirection(Down))
        }
        true
      }
    })
  }

  override def pause(): Unit = super.pause()

  override def resume(): Unit = super.resume()

  override def dispose(): Unit = super.dispose()
}




