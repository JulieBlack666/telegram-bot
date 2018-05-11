package bot

import java.text.SimpleDateFormat
import java.util.Date
import scala.collection.immutable

object  Commands {

  var _polls: Map[Int, Poll] = immutable.Map[Int, Poll]()
  var maxId = 0
  private val dateFormat = new SimpleDateFormat("hh:mm:ss yy:MM:dd")

  def parseTime(time : Option[String]) : Date = {
    dateFormat.parse(time.getOrElse(return null))
  }

  case class CreatePoll(name : String, anonimity : Option[String], continuousVisibility : Option[String],
                        startTime : Option[String], stopTime : Option[String]) extends Command {
    override def getReply: String = {
      val anonimityValue = anonimity.getOrElse("yes") == "yes"
      val continuousVisibilityValue = continuousVisibility.getOrElse("afterstop") == "continuous"
      val startTimeValue = parseTime(startTime)
      val stopTimeValue = parseTime(stopTime)
      val id = maxId
      maxId += 1

      _polls = _polls + (id -> Poll(name, id, anonimityValue, continuousVisibilityValue, startTimeValue, stopTimeValue))
      id.toString
    }
  }

  case class ListPolls() extends Command {
    override def getReply: String = {
      if (_polls.isEmpty) "You have no polls"
      else "Current polls:\n" + _polls.map{case (k, v) => "  " + k + " : " + v.name}.mkString("\n")
    }
  }

  case class DeletePoll(id : Int) extends Command {
    override def getReply: String = {
      _polls.get(id).map(_ => {
        _polls = _polls - id
        "Poll deleted successfully"
      }).getOrElse("Poll does not exist")
    }
  }

  case class StartPoll(id : Int) extends Command {
    override def getReply: String = {
      _polls.get(id).map(_ => {
        val currentPoll = _polls(id)
        val newPoll = currentPoll.start()
        if (newPoll == currentPoll)
          "Sorry, cannot start poll if it is active or a start time is defined"
        else {
          _polls = _polls + (id -> newPoll)
          "The poll is started successfully"
        }
      }).getOrElse("Poll does not exist")
    }
  }

  case class StopPoll(id : Int) extends Command {
    override def getReply: String = {
      _polls.get(id).map(_ => {
        val currentPoll = _polls(id)
        val newPoll = currentPoll.stop()
        if (newPoll == currentPoll)
          "Sorry, cannot stop poll if it is not active or a stop time is defined"
        else {
          _polls = _polls + (id -> newPoll)
          "The poll is stopped successfully"
        }
      }).getOrElse("Poll does not exist")
    }
  }

  case class PollResult(id : Int) extends Command {
    override def getReply: String = {
      _polls.get(id).map(_ => {
        _polls(id).getResult
      }).getOrElse("Poll does not exist")
    }
  }

  case class BadRequest() extends Command {
    override def getReply: String = "Bad request"
  }
}
