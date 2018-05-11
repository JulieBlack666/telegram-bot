package bot

import bot.QuestionType.QuestionType

class Question(name : String, q_type : QuestionType, variants : List[String])

object QuestionType extends Enumeration {
  type QuestionType = Value
  val open: bot.QuestionType.Value = Value("open")
  val choice: bot.QuestionType.Value = Value("choice")
  val multi: bot.QuestionType.Value = Value("multi")
}