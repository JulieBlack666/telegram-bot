package app

import bot.Bot
import scala.io.Source

object FileInput {
  def main(args: Array[String]) {
    val filename = "input.txt"
    for (line <- Source.fromFile(filename).getLines) {
      println(Bot.handleCommand(line))
    }
  }
}
