package bot

import scala.io.Source


object App {

  private val _polls: scala.collection.immutable.Map[Int, Poll] = _

  def main(args: Array[String]) {
    println("Hello, world!")
    val parser = new CommandParser()
    val filename = "input.txt"
    for (line <- Source.fromFile(filename).getLines) {
      parser.apply(line)
    }
  }

  def createPoll(): Unit = {


  }
}
