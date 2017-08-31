package gdx.scala.demo

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.graphics.{Color, GL20, Pixmap, Texture}
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.{Rectangle, Vector2}

import scala.collection.mutable.ListBuffer

trait GridConstants {
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

class GdxScalaDemoGame extends ApplicationAdapter with GridConstants {
  private var batch: SpriteBatch = _
  private var snakeCell: Texture = _
  private var fruitCell: Texture = _

  val stageSize = new Rectangle(0, 0, 0, 0)
  var snakeDirection:Direction = Stop
  var nextDirection:Direction = Stop
  val snakePath = ListBuffer(new Vector2(0, 0))
  val fruitPos = new Vector2(0, 0)
  var snakeSpeed:Float = 3F
  var snakeSize:Int = 1

  def pathStep:Int = GRID_WIDTH / snakeSpeed.toInt

  def createSnakeCell():Texture = {
    val pm = new Pixmap(GRID_WIDTH, GRID_WIDTH, Format.RGBA8888)
    pm.setColor(SNAKE_COLOR)
    pm.fillRectangle(0, 0, GRID_WIDTH, GRID_WIDTH)
    val t = new Texture(pm)
    pm.dispose()
    t
  }

  def createFruitCell():Texture = {
    val pm = new Pixmap(GRID_WIDTH, GRID_WIDTH, Format.RGBA8888)
    pm.setColor(FRUIT_COLOR)
    pm.fillRectangle(0, 0, GRID_WIDTH, GRID_WIDTH)
    val t = new Texture(pm)
    pm.dispose()
    t
  }

  def putFruit() = {
    (fruitPos.x, fruitPos.y) match {
      case (0, 0) => { // initializing
        fruitPos.x = GRID_WIDTH * 12
        fruitPos.y = GRID_WIDTH * 15
      }
      case _ => { // when eaten
        fruitPos.x = GRID_WIDTH * Math.floor(Math.random() * stageSize.width / GRID_WIDTH).toFloat
        fruitPos.y = GRID_WIDTH * Math.floor(Math.random() * stageSize.height / GRID_WIDTH).toFloat
      }
    }
  }

  /**
    * Initialize the game screen
    */
  override def create(): Unit = {
    batch = new SpriteBatch
    snakeCell = createSnakeCell()
    fruitCell = createFruitCell()
    putFruit()
  }

  private def processKeyEvent(): Unit = {
    val ipt = Gdx.input
    if (ipt.isKeyPressed(Keys.LEFT)) {
      nextDirection = Left
    } else if (ipt.isKeyPressed(Keys.RIGHT)) {
      nextDirection = Right
    } else if (ipt.isKeyPressed(Keys.UP)) {
      nextDirection = Up
    } else if (ipt.isKeyPressed(Keys.DOWN)) {
      nextDirection = Down
    }
  }

  private def updatePosition(): Unit = {
    val top = snakePath.head
    snakePath.insert(0,
      new Vector2(top.x + snakeDirection.x * snakeSpeed,
                  top.y + snakeDirection.y * snakeSpeed))

    val toRemove = snakePath.length - snakeSize * pathStep
    if (toRemove > 0) {
      snakePath.remove(snakePath.length - toRemove, toRemove)
    }
    val nextTop = snakePath.head
    if (nextTop.x.toInt % GRID_WIDTH == 0 && nextTop.y.toInt % GRID_WIDTH == 0) {
      snakeDirection = nextDirection
    }
  }

  private def judge(): Unit = {
    val top = snakePath.head
    if (top.equals(fruitPos)) {
      snakeSize += GROWTH_SIZE
      putFruit()
    }
  }

  private def renderSnake(): Unit = {
    val step = pathStep
    for (i <- snakePath.indices if i % step == 0) {
      val pos = snakePath(i)
      batch.draw(snakeCell, pos.x, pos.y)
    }
  }

  private def renderFruit(): Unit = {
    batch.draw(fruitCell, fruitPos.x, fruitPos.y)
  }

  override def render(): Unit = {
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
    stageSize.width = width
    stageSize.height = height
  }

  override def pause(): Unit = super.pause()

  override def resume(): Unit = super.resume()

  override def dispose(): Unit = super.dispose()
}




