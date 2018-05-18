package bot

import java.util.{Calendar, Date}

case class Poll(name : String, id : Int, anonymity : Boolean = true,
                continuous_visibility : Boolean = false,
                start_time : Date = null, end_time : Date = null,
                active : Boolean = false, questions : List[Question] = List()) {
  // .copy case classes
  // pattern matching flag => ...

  def start(): Poll = {
    if (start_time != null || active)
      this
    else
      this.copy(active = true)
  }

  def stop(): Poll = {
    if (end_time != null || !active)
      this
    else
      this.copy(active = false)
  }

  def getResult : String = {
    if (active && !continuous_visibility)
      "Can't see before finished"
    else
      "The poll " + name + " has following result:"
  }

  def addQuestion(question: Question): Poll ={
    this.copy(questions = this.questions :+ question)
  }

  def deleteQuestion(index : Int): Poll = {
    this.copy(questions = this.questions.patch(id, Nil, 1))
  }

  def getQuestion(index: Int): Option[Question] = {
    if (index > questions.size) {
      None
    } else Some(questions(index))
  }

  def answerQuestion(index: Int, answer: String) : Option[Poll] = {
    val question = getQuestion(index).getOrElse(return None)
    Some(this.copy(questions = this.questions.patch(index, List(question.answer(answer)), 1)))
  }

  override def toString: String = {
    questions.mkString(" ")
  }
}
