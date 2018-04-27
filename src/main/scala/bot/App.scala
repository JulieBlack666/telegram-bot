package bot

import java.text.SimpleDateFormat

import scala.collection.immutable
import scala.io.Source
import scala.util.Try


object App {

  private var _polls = immutable.Map[Int, Poll]()
  private var max_id = 0

  def main(args: Array[String]) {
    val filename = "input.txt"
    for (line <- Source.fromFile(filename).getLines) {
      CommandParser.apply(line)
    }
  }

  def createPoll(name : String, anonimityStr : Option[String], continuous_visibilityStr : Option[String],
                 startTimeStr : Option[String], stopTimeStr : Option[String]): Unit = {
    val anonimity = anonimityStr.getOrElse("yes") == "yes"
    val continuous_visibility = continuous_visibilityStr.getOrElse("afterstop") == "continuous"
    val format = new SimpleDateFormat("hh:mm:ss yy:MM:dd")
    val startTime = format.parse(startTimeStr.getOrElse("00:00:00 00:00:00"))
    val stopTime = format.parse(stopTimeStr.getOrElse("00:00:00 00:00:00")) //TODO getOrElse()

    val id = max_id
    max_id += 1
    _polls = _polls + (id -> new Poll(name, id, anonimity, continuous_visibility, startTime, stopTime))
    println(id)
  }

  def listPolls(): Unit = {
    println("current polls:")
    _polls.foreach(x => println(x._1 + " : " + x._2.name))
  }

  def deletePoll(id : Int): Unit = { // TODO filter
    if(_polls.contains(id)) {
      _polls = _polls - id
      println("Poll deleted successfully")
    } else {
      println("Error : poll is not exist")
    }
  }

  def startPoll(id : Int): Unit = {
    if(_polls.contains(id)) {
      _polls(id).start()
      println("The poll is started successfully")
    } else {
      println("Error : poll is not exist")
    }
  }

  def stopPoll(id : Int): Unit = {
    if(_polls.contains(id)) {
      _polls(id).stop()
      println("The poll is stopped successfully")
    } else {
      println("Error : poll is not exist")
    }
  }

  //TODO private

  def pollResult(id : Int): Unit = {
    if(_polls.contains(id)) {
      println(_polls(id).getResult)
    } else {
      println("Error : poll is not exist")
    }
  }
}
