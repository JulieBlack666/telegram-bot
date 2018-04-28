package bot

import bot.Commands._
import bot.ContextCommands._

import scala.util.parsing.combinator.RegexParsers

class CommandParser extends RegexParsers {
  def createPoll: Parser[Command] = {
    val pollName = Parser("""\w+""".r)
    val anonimity = Parser("(" ~> ("yes" | "no") <~ ")")
    val continuous = Parser("(" ~> ("afterstop" | "continuous") <~ ")")
    val startTime = Parser("(" ~> """\d{2}:\d{2}:\d{2} \d{2}:\d{2}:\d{2}""".r <~ ")")
    val stopTime = startTime
    ("/create_poll (" ~> pollName <~ ")") ~ anonimity.? ~ continuous.? ~ startTime.? ~ stopTime.? ^^
      { s =>  CreatePoll( s._1._1._1._1, s._1._1._1._2, s._1._1._2,
        s._1._2, s._2)}
  }

  def listPolls: Parser[Command] = """^/list""".r ^^ { _ => ListPolls() }
  def deletePoll: Parser[Command] = "/delete_poll (" ~> """\d+""".r <~ ")" ^^ { x => DeletePoll(x.toInt) }
  def startPoll: Parser[Command] = "/start_poll (" ~> """\d+""".r <~ ")" ^^ { x => StartPoll(x.toInt) }
  def stopPoll: Parser[Command] = "/stop_poll (" ~> """\d+""".r <~ ")" ^^ { x => StopPoll(x.toInt) }
  def pollResult: Parser[Command] = "/result (" ~> """\d+""".r <~ ")" ^^ { x => PollResult(x.toInt) }
  def beginContext: Parser[Command] = "/begin (" ~> """\d+""".r <~ ")" ^^ { x => BeginContext(x.toInt) }
  def endContext: Parser[Command] = "/begin (" ~> """\d+""".r <~ ")" ^^ { x => EndContext() }
  def view: Parser[Command] = "/begin (" ~> """\d+""".r <~ ")" ^^ { x => View() }

  def apply(input: String): Command = parse(
    createPoll | listPolls | deletePoll | startPoll | stopPoll | pollResult, input)
  match {
    case Success(result, _) => result
    case failure : NoSuccess => BadRequest()
  }
}

object CommandParser {
  def apply: CommandParser = new CommandParser()
}
