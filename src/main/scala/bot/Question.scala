package bot

import bot.QuestionType.QuestionType

case class Question(name : String, q_type : QuestionType, variants : List[Variant]){
  def answer(answ: String): Question ={
    q_type match {
      case QuestionType.open => answerOpen(answ)
      case QuestionType.choice => answerChoice(answ)
      case QuestionType.multi => answerMulti(answ)
    }
  }

  def answerOpen(answer: String): Question = {
    this.copy(variants = this.variants :+ Variant(answer, 1))
  }

  def answerChoice(answer: String): Question ={
    val index = answer.toInt
    if (index >= variants.size)
      this
    else{
      val updatedVariant = variants(index).vote()
      this.copy(variants = this.variants.patch(index, List(updatedVariant), 1))
    }
  }

  def answerMulti(answer: String): Question = {
    val indices = answer.split(" ")
    var newQuestion = this
    for (index <- indices){
      newQuestion = newQuestion.answerChoice(index)
    }
    newQuestion
  }
}

case class Variant(variant : String, answCount : Int){
  def vote() : Variant = {
    this.copy(answCount = this.answCount + 1)
  }
}

object QuestionType extends Enumeration {
  type QuestionType = Value
  val open: bot.QuestionType.Value = Value("open")
  val choice: bot.QuestionType.Value = Value("choice")
  val multi: bot.QuestionType.Value = Value("multi")
}
