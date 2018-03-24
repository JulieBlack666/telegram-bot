package bot

import java.text.SimpleDateFormat

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

  def createPoll(name : String, anonimityStr : String, continuous_visibilityStr : String, startTimeStr : String,
                 stopTimeStr : String): Unit = {
    val anonimity = if (anonimityStr == "yes") true else false
    val continuous_visibility = if (continuous_visibilityStr == "continuous") true else false
    val format = new SimpleDateFormat("hh:mm:ss yy:MM:dd")
    val startTime = if (startTimeStr != null) format.parse(startTimeStr) else null
    val stopTime = if (stopTimeStr != null) format.parse(stopTimeStr) else null
    val id = max_id
    max_id = max_id + 1
    _polls = _polls + (id -> new Poll(name, id, anonimity, continuous_visibility, startTime, stopTime))
    println(id)
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
