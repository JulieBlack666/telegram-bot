package bot

import scala.util.parsing.combinator.RegexParsers

class CommandParser extends RegexParsers {
  def createPoll: Parser[Unit] = ("/create_poll (" ~> """\w+""".r <~ ")") ~
      ("(" ~> ("yes" | "no") <~ ")").? ~
      ("(" ~> ("afterstop" | "continuous") <~ ")").? ^^ { s => App.createPoll(s._1._1, s._1._2 ++ s._2)}
  def listPolls: Parser[Unit] = """^/list""".r ^^ { _ => App.listPolls() }
  def deletePoll: Parser[Unit] = "/delete_poll (" ~> """\d+""".r <~ ")" ^^ { d => App.deletePoll(d.toInt) }
  def startPoll: Parser[Unit] = "/start_poll (" ~> """\d+""".r <~ ")" ^^ { d => App.startPoll(d.toInt) }
  def stopPoll: Parser[Unit] = "/stop_poll (" ~> """\d+""".r <~ ")" ^^ { d => App.stopPoll(d.toInt) }
  def pollResult: Parser[Unit] = "/result (" ~> """\d+""".r <~ ")" ^^ { d => App.pollResult(d.toInt) }



  def apply(input: String): Unit = parseAll(
    createPoll | listPolls | deletePoll | startPoll | stopPoll | pollResult, input)
  match {
    case Success(result, _) => result
    case failure : NoSuccess => println("no success")
  }
}
