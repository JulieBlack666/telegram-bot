package bot

import bot.Commands._polls

object ContextCommands {

  var selectedPoll : Option[Int] = _

  def WithSelectedPoll(body: => String): String = {
    if (selectedPoll != null){
      body
    } else
      "You should select poll first"
  }

  case class BeginContext(id : Int) extends Command {
    override def getReply(user: User): String = {
      _polls.get(id).map(_ =>{
        selectedPoll = Some(id)
        "Now you are working with context of the poll " + _polls(id).name }
      ).getOrElse("The poll does not exist")
    }
  }

  case class EndContext() extends Command {
    override def getReply(user: User): String = {
      WithSelectedPoll {
        val pollToEnd = _polls(selectedPoll.get)
        selectedPoll = null
        "You stopped working with the poll " + pollToEnd.name
      }
    }
  }

  case class View() extends Command {
    override def getReply(user: User): String = {
      WithSelectedPoll {
        _polls(selectedPoll.get).toString
      }
    }
  }

  case class AddQuestion(name: String, qtype: String, variants: List[String]) extends Command {
    override def getReply(user: User): String = {
      WithSelectedPoll {
        val question = Question(name, QuestionType.withName(qtype), variants.map(v => Variant(v, 0)))
        val newPoll = _polls(selectedPoll.get).addQuestion(question)
        _polls = _polls + (selectedPoll.get -> newPoll)
        "Question added successfully"
      }
    }
  }

  case class DeleteQuestion(index : Int) extends Command{
    override def getReply(user: User): String = {
      WithSelectedPoll {
        val newPoll = _polls(selectedPoll.get).deleteQuestion(index)
        _polls = _polls + (selectedPoll.get -> newPoll)
        "The question has been deleted"
      }
    }
  }
  case class AnswerQuestionOpen(index : Int, answer: String) extends Command{
    override def getReply(user: User): String = {
      WithSelectedPoll {
        if (!_polls(selectedPoll.get).active)
          "Poll is not active yet"
        else if (_polls(selectedPoll.get).getQuestion(index).getOrElse(return "No such question").q_type == QuestionType.open) {
          val newPoll = _polls(selectedPoll.get).answerQuestion(index, answer, user).getOrElse(return "No such question")
          _polls = _polls + (selectedPoll.get -> newPoll)
          "Your answer has been recorded"
        }
        else
          "Wrong question type"
      }
    }
  }

  case class AnswerQuestionChoiceMulti(index : Int, answer: List[String]) extends Command{
    override def getReply(user: User): String = {
      WithSelectedPoll {
        val questionType = _polls(selectedPoll.get).getQuestion(index).getOrElse(return "No such question").q_type
        if (!_polls(selectedPoll.get).active)
          "Poll is not active yet"
        else if (questionType == QuestionType.choice && answer.size == 1 || questionType == QuestionType.multi) {
          val newPoll = _polls(selectedPoll.get).answerQuestion(index, answer.mkString(" "), user).getOrElse(return "No such question")
          _polls = _polls + (selectedPoll.get -> newPoll)
          "Your answer has been recorded"
        }
        else
          "Can"
      }
    }
  }
}
