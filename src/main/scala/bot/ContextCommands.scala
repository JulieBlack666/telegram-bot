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
        "Sorry, no poll is selected"
    }
  }

  case class View() extends Command {
    override def getReply: String = {
      selectedPoll._2.questions.mkString(" ")
    }
  }

  case class AddQuestion(name: String, qtype: String, variants: List[String]) extends Command {
    override def getReply: String = {
      if (selectedPoll != null) {
        val question = Question(name, QuestionType.withName(qtype), variants.map(v => Variant(v, 0)))
        val newPoll = selectedPoll._2.addQuestion(question)
        val pollId = selectedPoll._1
        _polls = _polls + (pollId -> newPoll)
        selectedPoll = (pollId, newPoll)
        "Question added successfully"
      }
      else
        "You should select poll first"
    }
  }

  case class DeleteQuestion(id : Int) extends Command{
    override def getReply: String = {
      if (selectedPoll != null) {
        val newPoll = selectedPoll._2.deleteQuestion(id)
        val pollId = selectedPoll._1
        _polls = _polls + (pollId -> newPoll)
        selectedPoll = (pollId, newPoll)
        "The question has been deleted"
      }
      else
        "You chould select poll first"
    }
  }
  case class AnswerQuestionOpen(id : Int, answer: String) extends Command{
    override def getReply: String = {
      answer
    }
  }

  case class AnswerQuestionChoiceMulti(id : Int, answer: List[String]) extends Command{
    override def getReply: String = {
      answer.mkString(" ")
    }
  }
}
