package bot

import bot.QuestionType.QuestionType

case class Question(name : String, q_type : QuestionType, variants : List[Variant], voters : List[User] = List()){

  def answer(answ: String, user: User): Question ={
    val newQuestion = q_type match {
      case QuestionType.open => answerOpen(answ)
      case QuestionType.choice => answerChoice(answ)
      case QuestionType.multi => answerMulti(answ)
    }
    newQuestion.copy(voters = this.voters :+ user)
  }

  def answerOpen(answer: String): Question = {
    this.copy(variants = this.variants :+ Variant(answer, 1))
  }

  def answerChoice(answer: String): Question ={
    val index = answer.toInt - 1
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

  def makeString(noResult : Boolean, anonymity : Boolean): String = {
    if (q_type == QuestionType.open)
      s"""Question: $name
         |type: $q_type
         |""".stripMargin
    else {
      val variants_str = noResult match {
        case true => variants.zipWithIndex
          .map { case (x, i) => (i+1).toString + ") " + x.variant }.mkString("\n")
        case false => variants.zipWithIndex
        .map { case (x, i) => (i+1).toString + ") " + x.toString }.mkString("\n")
      }
      val answered = if (!anonymity)
        "answered users: " + voters.map(x => x.name).mkString(", ")
      else ""
      s"""Question: $name
         |type: $q_type
         |variants:
         |$variants_str
         |$answered""".stripMargin
      }
  }
}

case class Variant(variant : String, answCount : Int){
  def vote() : Variant = {
    this.copy(answCount = this.answCount + 1)
  }

  override def toString: String = {
    s"$variant: $answCount "
  }
}

object QuestionType extends Enumeration {
  type QuestionType = Value
  val open: bot.QuestionType.Value = Value("open")
  val choice: bot.QuestionType.Value = Value("choice")
  val multi: bot.QuestionType.Value = Value("multi")
}
