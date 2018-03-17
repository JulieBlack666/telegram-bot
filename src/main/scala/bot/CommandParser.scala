package bot

import scala.util.parsing.combinator.RegexParsers

class CommandParser extends RegexParsers {
  def create: Parser[Unit] = """/create""".r ^^ { _ => action }

  def action: Unit = {
    println("test")
  }

  def apply(input: String): Unit = parseAll(create, input) match {
    case Success(result, _) => result
    case failure : NoSuccess => println("no success")
  }
}
