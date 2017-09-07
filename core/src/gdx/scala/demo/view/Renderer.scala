package gdx.scala.demo.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.{GL20, Pixmap, Texture}
import gdx.scala.demo.logic.GameSystem.{FRUIT_COLOR, GRID_WIDTH, SNAKE_COLOR}
import gdx.scala.demo.logic.{FruitState, SnakeState, WorldContext}

class Renderer(batch:SpriteBatch) {
  private lazy val snakeTexture = {
    val pm = new Pixmap(GRID_WIDTH, GRID_WIDTH, Format.RGBA8888)
    pm.setColor(SNAKE_COLOR)
    pm.fillRectangle(0, 0, GRID_WIDTH, GRID_WIDTH)
    val t = new Texture(pm)
    pm.dispose()
    t
  }

  private lazy val fruitTexture = {
    val pm = new Pixmap(GRID_WIDTH, GRID_WIDTH, Format.RGBA8888)
    pm.setColor(FRUIT_COLOR)
    pm.fillRectangle(0, 0, GRID_WIDTH, GRID_WIDTH)
    val t = new Texture(pm)
    pm.dispose()
    t
  }

  private def renderSnake(snake:SnakeState): Unit = {
    val step = (GRID_WIDTH / snake.speed).toInt
    snake.path.zipWithIndex.filter(_._2 % step == 0).foreach {
      case (pos, _) => batch.draw(snakeTexture, pos.x, pos.y)
    }
  }

  private def renderFruit(fruit:FruitState): Unit = {
    batch.draw(fruitTexture, fruit.position.x, fruit.position.y)
  }

  def render(snake:SnakeState, fruit:FruitState, world:WorldContext):Unit = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    batch.begin()
    renderSnake(snake)
    renderFruit(fruit)
    batch.end()

  }
}
