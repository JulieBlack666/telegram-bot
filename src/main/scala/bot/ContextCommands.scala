package bot

import bot.Commands._polls

object ContextCommands {

  var selectedPoll : (Int, Poll) = _

  case class BeginContext(id : Int) extends Command {
    override def getReply: String = {
      _polls.get(id).map(_ =>{
        selectedPoll = (id, _polls(id))
        "Now you are working with context of the poll " + _polls(id).name
      }
      ).getOrElse("The poll does not exist")
    }
  }

  case class EndContext() extends Command {
    override def getReply: String = {
      if (selectedPoll != null){
        selectedPoll = null
        "You stopped working with the poll " + selectedPoll._2.name
      } else
        "Sorry, no context is open"
    }
  }

  case class View() extends Command {
    override def getReply: String = {
      "Красивое представление опроса"
    }
  }

  case class AddQuestion(name: String, qtype: String, variants : List[String]) extends Command {
    override def getReply: String = {
      ""
    }
  }

  case class DeleteQuestion(id : Int) extends Command{
    override def getReply: String = {
      val newPoll = selectedPoll._2.deleteQuestion(id)
      _polls = _polls + (selectedPoll._1 -> newPoll)
      "The question has been deleted"
    }
  }
}
