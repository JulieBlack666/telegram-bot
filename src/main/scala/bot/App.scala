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

  def deletePoll(id : String): Unit = {
    println("deletePoll")
  }

  def startPoll(id : String): Unit = {
    println("startPoll")
  }

  def stopPoll(id : String): Unit = {
    println("stopPoll")
  }

  def pollResult(id : String): Unit = {
    println("pollResult")
  }
}
