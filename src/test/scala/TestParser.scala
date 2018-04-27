import bot.CommandParser
import org.scalatest.FlatSpec


class TestParser extends FlatSpec  {
  "List Polls" should "be empty" in {
    assert(CommandParser.apply("/list") == "")
  }
}
