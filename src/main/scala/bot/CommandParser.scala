package bot

import scala.util.parsing.combinator.RegexParsers

class CommandParser extends RegexParsers {
  def createPoll: Parser[Unit] = """^/create (\w+)$""".r ^^ { s => App.createPoll(s) }
  def listPolls: Parser[Unit] = """^/list""".r ^^ { _ => App.listPolls() }
  def deletePoll: Parser[Unit] = """^/delete_poll (\d+)$""".r ^^ { d => App.deletePoll(d) }
  def startPoll: Parser[Unit] = """^/start_poll (\d+)$""".r ^^ { d => App.startPoll(d) }
  def stopPoll: Parser[Unit] = """^/stop_poll (\d+)$""".r ^^ { d => App.stopPoll(d) }
  def pollResult: Parser[Unit] = """^/result (\d+)$""".r ^^ { d => App.pollResult(d) }



  def apply(input: String): Unit = parseAll(
    createPoll | listPolls | deletePoll | startPoll | stopPoll | pollResult, input)
  match {
    case Success(result, _) => result
    case failure : NoSuccess => println("no success")
  }
}
