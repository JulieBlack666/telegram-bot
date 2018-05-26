package bot

import java.text.SimpleDateFormat
import java.util.Date
import scala.collection.immutable

object  Commands {

  var _polls: Map[Int, Poll] = immutable.Map[Int, Poll]()
  val idIterator : Iterator[Int] = Stream.from(0).iterator
  private val dateFormat = new SimpleDateFormat("HH:mm:ss yy:MM:dd")

  def getNextId: Int = idIterator.next()

  def parseTime(time : Option[String]) : Option[Date] = {
    Some(dateFormat.parse(time.getOrElse(return None)))
  }

  case class CreatePoll(name : String, anonimity : Option[String], continuousVisibility : Option[String],
                        startTime : Option[String], stopTime : Option[String]) extends Command {
    override def getReply(user: User): String = {
      val anonimityValue = anonimity.getOrElse("yes") == "yes"
      val continuousVisibilityValue = continuousVisibility.getOrElse("afterstop") == "continuous"
      val startTimeValue = parseTime(startTime)
      val stopTimeValue = parseTime(stopTime)
      val id = getNextId

      _polls = _polls + (id -> Poll(name, id, anonimityValue, continuousVisibilityValue, startTimeValue, stopTimeValue))
      "Poll created. Poll id: " + id.toString
    }
  }

  case class ListPolls() extends Command {
    override def getReply(user: User): String = {
      if (_polls.isEmpty) "You have no polls"
      else "Current polls:\n" + _polls.map{case (k, v) => "  " + k + " : " + v.name}.mkString("\n")
    }
  }

  case class DeletePoll(id : Int) extends Command {
    override def getReply(user: User): String = {
      _polls.get(id).map(_ => {
        _polls = _polls - id
        "Poll deleted successfully"
      }).getOrElse("Poll does not exist")
    }
  }

  case class StartPoll(id : Int) extends Command {
    override def getReply(user: User): String = {
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
    override def getReply(user: User): String = {
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
    override def getReply(user: User): String = {
      _polls.get(id).map(_ => {
        _polls(id).getResult
      }).getOrElse("Poll does not exist")
    }
  }

  case class BadRequest() extends Command {
    override def getReply(user: User): String = "Bad request"
  }
}
