package bot_tests

import bot.CommandParser
import org.scalatest.FlatSpec

class TestOutput extends FlatSpec {
  "List Polls" should "be empty" in {
    assert(CommandParser.apply("/list").getReply == "You have no polls")
  }
}
