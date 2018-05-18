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

  case class DeleteQuestion(index : Int) extends Command{
    override def getReply: String = {
      withSelectedPoll {
        val newPoll = selectedPoll.deleteQuestion(index)
        val pollId = selectedPoll.id
        _polls = _polls + (pollId -> newPoll)
        selectedPoll =  newPoll
        "The question has been deleted"
      }
    }
  }
  case class AnswerQuestionOpen(index : Int, answer: String) extends Command{
    override def getReply: String = {
      withSelectedPoll {
        if (selectedPoll.getQuestion(index).getOrElse(return "No such question").q_type == QuestionType.open) {
          selectedPoll = selectedPoll.answerQuestion(index, answer).getOrElse(return "No such question")
          _polls = _polls + (selectedPoll.id -> selectedPoll)
          "Your answer has been recorded"
        }
        else
          "Wrong question type"
      }
    }
  }

  case class AnswerQuestionChoiceMulti(index : Int, answer: List[String]) extends Command{
    override def getReply: String = {
      withSelectedPoll {
        val questionType = selectedPoll.getQuestion(index).getOrElse(return "No such question").q_type
        if (questionType == QuestionType.choice && answer.size == 1 || questionType == QuestionType.multi) {
          selectedPoll = selectedPoll.answerQuestion(index, answer.mkString(" ")).getOrElse(return "No such question")
          _polls = _polls + (selectedPoll.id -> selectedPoll)
          "Your answer has been recorded"
        }
        else
          "Wrong question type"
      }
    }
  }
}
