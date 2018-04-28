package bot

import bot.QuestionType.QuestionType

class Question(name : String, q_type : QuestionType) {

}

object QuestionType extends Enumeration {
  type QuestionType = Value
  val open = Value("open")
  val choice = Value("choice")
  val multi = Value("multi")
}