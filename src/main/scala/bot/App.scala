package bot

import java.text.SimpleDateFormat
import java.util.Date

import scala.collection.immutable
import scala.io.Source
import scala.util.Try


object App {

  private var _polls = immutable.Map[Int, Poll]()
  private var max_id = 0
  private val dateFormat = new SimpleDateFormat("hh:mm:ss yy:MM:dd")

  def main(args: Array[String]) {
    val filename = "input.txt"
    for (line <- Source.fromFile(filename).getLines) {
      CommandParser.apply(line)
    }
  }

  def parseTime(time : Option[String]) : Date = {
    if (time.isDefined)
      dateFormat.parse(time.get)
    null
  }

  def createPoll(name : String, anonimity : Option[String], continuousVisibility : Option[String],
                 startTime : Option[String], stopTime : Option[String]): String = {
    val anonimityValue = anonimity.getOrElse("yes") == "yes"
    val continuousVisibilityValue = continuousVisibility.getOrElse("afterstop") == "continuous"
    val startTimeValue = parseTime(startTime)
    val stopTimeValue = parseTime(stopTime)
    val id = max_id
    max_id += 1

    _polls = _polls + (id -> new Poll(name, id, anonimityValue, continuousVisibilityValue, startTimeValue, stopTimeValue))
    id.toString
  }

  def listPolls(): String = {
    "current polls:\n" + _polls.map{case (k, v) => k + " : " + v.name}.mkString("\n")
  }

  def deletePoll(id : Int): String = {
    _polls.get(id).map(_ => {
      _polls = _polls - id
      "Poll deleted successfully"
    }).getOrElse("Poll does not exist")
  }

  def startPoll(id : Int): String = {
    _polls.get(id).map(_ => {
      _polls(id).start()
      "The poll is started successfully"
    }).getOrElse("Poll does not exist")
  }

  def stopPoll(id : Int): String = {
    _polls.get(id).map(_ => {
      _polls(id).stop()
      "The poll is stopped successfully"
    }).getOrElse("Poll does not exist")
  }

  def pollResult(id : Int): String = {
    _polls.get(id).map(_ => {
      _polls(id).getResult
    }).getOrElse("Poll does not exist")
  }
}
