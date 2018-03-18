package bot

import scala.collection.immutable
import scala.io.Source


object App {

  private var _polls = immutable.Map[Int, Poll]()
  private var max_id = 0

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
    val id = max_id
    max_id = max_id + 1
    _polls = _polls + (id -> new Poll(query, id))
  }

  def listPolls(): Unit = {
    println("listPolls")
    _polls.foreach(x => println(x._1 + " : " + x._2.name))
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
