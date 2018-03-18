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
    _polls.foreach(x => println(x._1 + " : " + x._2.name))
  }

  def deletePoll(id : Int): Unit = {
    if(_polls.contains(id)) {
      _polls = _polls - id
      println("Poll deleted successfully")
    } else {
      println("Error : poll is not exist")
    }
  }

  def startPoll(id : Int): Unit = {
    if(_polls.contains(id)) {
      println("The poll is started successfully")
    } else {
      println("Error : poll is not exist")
    }
  }

  def stopPoll(id : Int): Unit = {
    if(_polls.contains(id)) {
      println("The poll is stopped successfully")
    } else {
      println("Error : poll is not exist")
    }
  }

  def pollResult(id : Int): Unit = {
    if(_polls.contains(id)) {
      println("The poll '" + _polls(id).name + "' has following result: ")
    } else {
      println("Error : poll is not exist")
    }
  }
}
