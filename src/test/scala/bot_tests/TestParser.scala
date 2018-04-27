package bot_tests

import bot.CommandParser
import bot.Commands._
import org.scalatest.FlatSpec


class TestParser extends FlatSpec {
  "List Polls" should "be parsed" in {
    assert(CommandParser.apply("/list") == ListPolls())
  }
}
