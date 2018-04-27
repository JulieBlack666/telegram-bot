package bot

import scala.util.parsing.combinator.RegexParsers

class CommandParser extends RegexParsers {
  def createPoll: Parser[String] = {
    val pollName = Parser("""\w+""".r)
    val anonimity = Parser("(" ~> ("yes" | "no") <~ ")")
    val continuous = Parser("(" ~> ("afterstop" | "continuous") <~ ")")
    val startTime = Parser("(" ~> """\d{2}:\d{2}:\d{2} \d{2}:\d{2}:\d{2}""".r <~ ")")
    val stopTime = startTime
    ("/create_poll (" ~> pollName <~ ")") ~ anonimity.? ~ continuous.? ~ startTime.? ~ stopTime.? ^^
      { s =>  App.createPoll( s._1._1._1._1, s._1._1._1._2, s._1._1._2,
        s._1._2, s._2)}
  }

  def listPolls: Parser[String] = """^/list""".r ^^ { _ => App.listPolls() }
  def deletePoll: Parser[String] = "/delete_poll (" ~> """\d+""".r <~ ")" ^^ { d => App.deletePoll(d.toInt) }
  def startPoll: Parser[String] = "/start_poll (" ~> """\d+""".r <~ ")" ^^ { d => App.startPoll(d.toInt) }
  def stopPoll: Parser[String] = "/stop_poll (" ~> """\d+""".r <~ ")" ^^ { d => App.stopPoll(d.toInt) }
  def pollResult: Parser[String] = "/result (" ~> """\d+""".r <~ ")" ^^ { d => App.pollResult(d.toInt) }

  def apply(input: String): String = parse(
    createPoll | listPolls | deletePoll | startPoll | stopPoll | pollResult, input)
  match {
    case Success(result, _) => result
    case failure : NoSuccess => "no success"
  }
}

object CommandParser {
  def apply: CommandParser = new CommandParser()
}
