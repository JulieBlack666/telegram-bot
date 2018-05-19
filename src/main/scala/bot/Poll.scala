package bot

import java.util.Date

case class Poll(name : String, id : Int, anonymity : Boolean = true,
                continuous_visibility : Boolean = false,
                start_time : Date = null, end_time : Date = null,
                active : Boolean = false, questions : List[Question] = List()) {

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
    else if (questions.isEmpty) {
      "There is no questions yet"
    } else {
      val questions_pretty = questions.map(x => x.toString).mkString("\n")
      s"""The poll $name has following result:
         |$questions_pretty
       """.stripMargin
    }
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

  def answerQuestion(index: Int, answer: String, user: User) : Option[Poll] = {
    val question = getQuestion(index).getOrElse(return None)
    Some(this.copy(questions = this.questions.patch(index, List(question.answer(answer, user)), 1)))
  }

  override def toString: String = {
    val is_active = if (active) "active now" else "is not active"
    val is_anon = if (anonymity) "poll is anonymous" else "poll is not anonymous"
//    val questions_pretty = questions.map(x => x.name).mkString("\n")
    s"""Poll: $name id: $id
      |start time: $start_time end time: $end_time
      |$is_active
      |$is_anon""".stripMargin
//    questions.mkString(" ")
  }
}
