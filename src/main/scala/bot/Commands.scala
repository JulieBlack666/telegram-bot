package bot

import java.text.SimpleDateFormat
import java.util.Date
import scala.collection.immutable

object  Commands {

  var _polls = immutable.Map[Int, Poll]()
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
      val id = _polls.keys.reduceOption(_ max _).getOrElse(-1) + 1

      _polls = _polls + (id -> new Poll(name, id, anonimityValue, continuousVisibilityValue, startTimeValue, stopTimeValue))
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
        _polls(id).start()
      }).getOrElse("Poll does not exist")
    }
  }

  case class StopPoll(id : Int) extends Command {
    override def getReply: String = {
      _polls.get(id).map(_ => {
        _polls(id).stop()
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
