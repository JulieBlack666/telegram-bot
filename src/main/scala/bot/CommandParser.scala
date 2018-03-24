package bot

import scala.util.parsing.combinator.RegexParsers

class CommandParser extends RegexParsers {
  def createPoll: Parser[Unit] = {
    val pollName = Parser("""\w+""".r)
    val anonimity = Parser("(" ~> ("yes" | "no") <~ ")")
    val continuous = Parser("(" ~> ("afterstop" | "continuous") <~ ")")
    val startTime = Parser("(" ~> """\d{2}:\d{2}:\d{2} \d{2}:\d{2}:\d{2}""".r <~ ")")
    val stopTime = startTime
    ("/create_poll (" ~> pollName <~ ")") ~ anonimity.? ~ continuous.? ~ startTime.? ~ stopTime.? ^^
      { s =>  App.createPoll(s._1._1._1._1, s._1._1._1._2.getOrElse("yes"), s._1._1._2.getOrElse("afterstop"),
        s._1._2.orNull, s._2.orNull)}
  }

  def listPolls: Parser[Unit] = """^/list""".r ^^ { _ => App.listPolls() }
  def deletePoll: Parser[Unit] = "/delete_poll (" ~> """\d+""".r <~ ")" ^^ { d => App.deletePoll(d.toInt) }
  def startPoll: Parser[Unit] = "/start_poll (" ~> """\d+""".r <~ ")" ^^ { d => App.startPoll(d.toInt) }
  def stopPoll: Parser[Unit] = "/stop_poll (" ~> """\d+""".r <~ ")" ^^ { d => App.stopPoll(d.toInt) }
  def pollResult: Parser[Unit] = "/result (" ~> """\d+""".r <~ ")" ^^ { d => App.pollResult(d.toInt) }

  def createPolltest: Parser[Unit] = {
    val pollName = Parser("""\w+""".r)
    val anonimity = Parser("(" ~> ("yes" | "no") <~ ")")
    val continuous = Parser("(" ~> ("afterstop" | "continuous") <~ ")")
    val startTime = Parser("(" ~> """\d{2}:\d{2}:\d{2} \d{2}:\d{2}:\d{2}""".r <~ ")")

    val stopTime = startTime
    ("/create_poll (" ~> pollName <~ ")") ~ (anonimity.? | (anonimity ~ continuous.?) ~ startTime.? ~ stopTime.?) ^^
      { s => println(s)}
  }

  def apply(input: String): Unit = parse(
    createPoll | listPolls | deletePoll | startPoll | stopPoll | pollResult, input)
  match {
    case Success(result, _) => result
    case failure : NoSuccess => println("no success")
  }
}
