package app

import bot.Bot
import scala.io.Source

object FileInput {
  def main(args: Array[String]) {
    val filename = "input.txt"
    Source.fromFile(filename).getLines.foreach(line =>
      println(Bot.handleCommand(line)))
  }
}
