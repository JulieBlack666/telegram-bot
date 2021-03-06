package bot

import bot.Commands._
import bot.ContextCommands._
import scala.util.Properties
import scala.util.matching.Regex
import scala.util.parsing.combinator.RegexParsers

class CommandParser extends RegexParsers {
  override protected val whiteSpace: Regex = """ +""".r

  def createPoll: Parser[Command] = {
    val pollName = Parser("""([^()]|\)\)|\(\()+""".r)
    val anonimity = Parser("(" ~> ("yes" | "no") <~ ")")
    val continuous = Parser("(" ~> ("afterstop" | "continuous") <~ ")")
    val startTime = Parser("(" ~> """\d{2}:\d{2}:\d{2} \d{2}:\d{2}:\d{2}""".r <~ ")")
    val stopTime = startTime
    ("^/create_poll".r ~> "(" ~> pollName <~ ")") ~ anonimity.? ~ continuous.? ~ startTime.? ~ stopTime.? <~ "$".r ^^
      {case name ~ anon ~ continuation ~ start ~ end =>
        CreatePoll(name.replace("((", "(").replace("))", ")"), anon, continuation, start, end)}
  }

  def addQuestion: Parser[Command] = {
    val questionName = Parser("(" ~> """([^()]|\)\)|\(\()+""".r <~ ")")
    val questionType = Parser("(" ~> ("open" | "choice" | "multi") <~ ")")
    val variant = Parser("\n" ~> ("(" ~> """([^()]|\)\)|\(\()+""".r  <~ ")"))
    val command = Parser("^/add_question".r ~> questionName)
    command ~ questionType ~ rep(variant) ^^ { case name ~ qType ~ variants =>
      AddQuestion(name.replace("((", "(").replace("))", ")"),
        qType, variants.map(s => s.replace("((", "(").replace("))", ")"))) }
  }

  def listPolls: Parser[Command] = """^/list""".r ^^ { _ => ListPolls() }

  def deletePoll: Parser[Command] = "^/delete_poll".r ~> "(" ~> """\d+""".r <~ ")" ^^ { x => DeletePoll(x.toInt) }

  def startPoll: Parser[Command] = "^/start_poll".r ~> "(" ~> """\d+""".r <~ ")" ^^ { x => StartPoll(x.toInt) }

  def stopPoll: Parser[Command] = "^/stop_poll".r ~> "(" ~> """\d+""".r <~ ")" ^^ { x => StopPoll(x.toInt) }

  def pollResult: Parser[Command] = "^/result".r ~> "(" ~> """\d+""".r <~ ")" ^^ { x => PollResult(x.toInt) }

  def beginContext: Parser[Command] = "^/begin".r ~> "(" ~> """\d+""".r <~ ")" ^^ { x => BeginContext(x.toInt) }

  def endContext: Parser[Command] = "^/end".r ^^ { x => EndContext() }

  def view: Parser[Command] = "^/view".r ^^ { x => View() }

  def delQuestion: Parser[Command] = "^/delete_question".r ~> "(" ~> """\d+""".r <~ ")" ^^ { x => DeleteQuestion(x.toInt) }

  def answerQuestionOpen: Parser[Command] = "^/answer".r ~> ("(" ~> """\d+""".r <~ ")") ~ ("(" ~> """([^()]|\)\)|\(\()+""".r <~ ")") ^^
    { x => AnswerQuestionOpen(x._1.toInt, x._2.replace("((", "(").replace("))", ")")) }

  def answerQuestionChoiceMulti: Parser[Command] = "^/answer".r ~> ("(" ~> """\d+""".r <~ ")") ~
    ("(" ~> rep("""\d+""".r) <~ ")") ^^ { x => AnswerQuestionChoiceMulti(x._1.toInt, x._2) }

  def apply(input: String): Command = parse(
    createPoll | listPolls | deletePoll | startPoll | stopPoll | pollResult | beginContext | endContext |
      view | addQuestion | delQuestion | answerQuestionChoiceMulti | answerQuestionOpen, input)
  match {
    case Success(result, _) => result
    case failure: NoSuccess => BadRequest()
  }
}

object CommandParser {
  def apply: CommandParser = new CommandParser()
}

