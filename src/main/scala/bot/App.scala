package bot

import scala.collection.immutable
import scala.io.Source


object App {

  private var _polls = immutable.Map[Int, Poll]()

  def main(args: Array[String]) {
    println("Hello, world!")
    val parser = new CommandParser()
    val filename = "input.txt"
    for (line <- Source.fromFile(filename).getLines) {
      parser.apply(line)
    }
  }

  def createPoll(query : String): Unit = {
    println(query)
    //_polls = _polls + (1 -> new Poll(name, anonymity))
  }

  def listPolls(): Unit = {
    println("listPolls")
  }

  def deletePoll(query : String): Unit = {
    println(query)
  }

  def startPoll(query : String): Unit = {
    println(query)
  }

  def stopPoll(query : String): Unit = {
    println(query)
  }

  def pollResult(query : String): Unit = {
    println(query)
  }
}
