package gdx.scala.demo.logic

abstract class Direction(val x:Float, val y:Float)
object Stop extends Direction(0, 0)
object Left extends Direction(-1, 0)
object Right extends Direction(1, 0)
object Up extends Direction(0, 1)
object Down extends Direction(0, -1)
