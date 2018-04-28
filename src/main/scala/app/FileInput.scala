package app

import bot.Bot._
import scala.io.Source

object FileInput {
  def main(args: Array[String]) {
    val filename = "input.txt"
    for (line <- Source.fromFile(filename).getLines) {
      println(handleCommand(line))
    }
  }
}
