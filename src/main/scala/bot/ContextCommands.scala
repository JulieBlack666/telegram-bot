package bot

import bot.Commands._polls

object ContextCommands {

  var selectedPoll : Poll = _

  def withSelectedPoll(body: => String): String = { //TODO название получше?
    if (selectedPoll != null){
      body
    } else
      "You should select poll first"
  }

  case class BeginContext(id : Int) extends Command {
    override def getReply: String = {
      _polls.get(id).map(_ =>{
        selectedPoll = _polls(id)
        "Now you are working with context of the poll " + _polls(id).name }
      ).getOrElse("The poll does not exist")
    }
  }

  case class EndContext() extends Command {
    override def getReply: String = {
      withSelectedPoll {
        selectedPoll = null
        "You stopped working with the poll " + selectedPoll.name
      }
    }
  }

  case class View() extends Command {
    override def getReply: String = {
      withSelectedPoll {
        selectedPoll.toString
      }
    }
  }

  case class AddQuestion(name: String, qtype: String, variants: List[String]) extends Command {
    override def getReply: String = {
      withSelectedPoll {
        val question = Question(name, QuestionType.withName(qtype), variants.map(v => Variant(v, 0)))
        val newPoll = selectedPoll.addQuestion(question)
        val pollId = selectedPoll.id
        _polls = _polls + (pollId -> newPoll)
        selectedPoll = newPoll
        "Question added successfully"
      }
    }
  }

  case class DeleteQuestion(id : Int) extends Command{
    override def getReply: String = {
      withSelectedPoll {
        val newPoll = selectedPoll.deleteQuestion(id)
        val pollId = selectedPoll.id
        _polls = _polls + (pollId -> newPoll)
        selectedPoll =  newPoll
        "The question has been deleted"
      }
    }
  }
  case class AnswerQuestionOpen(id : Int, answer: String) extends Command{
    override def getReply: String = {
      withSelectedPoll {
        ???
      }
    }
  }

  case class AnswerQuestionChoiceMulti(id : Int, answer: List[String]) extends Command{
    override def getReply: String = {
      withSelectedPoll {
        ???
      }
    }
  }
}
