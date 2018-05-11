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

  def deleteQuestion(id : Int): Poll ={
    this.copy(questions = this.questions.patch(id, Nil, 1))
  }
}
