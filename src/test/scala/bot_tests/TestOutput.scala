package bot_tests

import bot.CommandParser
import bot.ContextCommands._
import bot.Commands._
import org.scalatest.FlatSpec

class TestOutput extends FlatSpec {
  "List polls" should "be empty" in {
    assert(CommandParser.apply("/list").getReply == "You have no polls")
  }

  "Create bad poll" should "be bad request" in {
    assert(CommandParser.apply("/create_poll").getReply == "Bad request")
  }

  "List polls" should "be empty after bad request" in {
    assert(CommandParser.apply("/list").getReply == "You have no polls")
  }

  "Create poll" should "return id" in {
    assert(CommandParser.apply("/create_poll (poll_name)").getReply forall Character.isDigit)
  }

  "List polls" should "now be with poll" in {
    assert(CommandParser.apply("/list").getReply == "Current polls:\n  0 : poll_name")
  }

  "Delete poll" should "actually delete poll" in {
    assert(CommandParser.apply("/delete_poll (0)").getReply == "Poll deleted successfully")
    assert(CommandParser.apply("/list").getReply == "You have no polls")
  }

  "Result" should "not be seen if poll is started and afterstop" in {
    CommandParser.apply("/create_poll (afterstop_poll) (yes) (afterstop)").getReply
    assert(CommandParser.apply("/list").getReply == "Current polls:\n  1 : afterstop_poll")
    assert(CommandParser.apply("/start_poll (1)").getReply == "The poll is started successfully")
    assert(CommandParser.apply("/result (1)").getReply == "Can't see before finished")
  }

  it should "give a result if poll is stopped" in {
    CommandParser.apply("/stop_poll (1)").getReply
    assert(CommandParser.apply("/result (1)").getReply == "The poll afterstop_poll has following result:")
  }

  it should "give results for continuous poll in any situation" in {
    CommandParser.apply("/create_poll (continuous_poll) (yes) (continuous)").getReply
    assert(CommandParser.apply("/result (2)").getReply == "The poll continuous_poll has following result:")
    CommandParser.apply("/start_poll (2)").getReply
    assert(CommandParser.apply("/result (2)").getReply == "The poll continuous_poll has following result:")
    CommandParser.apply("/stop_poll (2)").getReply
    assert(CommandParser.apply("/result (2)").getReply == "The poll continuous_poll has following result:")
  }

  "Start poll" should "work for usual poll" in {
    CommandParser.apply("/create_poll (usual_poll)").getReply
    assert(CommandParser.apply("/start_poll (3)").getReply == "The poll is started successfully")
  }

  it should "not work for poll with defined start time" in {
    CommandParser.apply("/create_poll (poll_with_defined_start_time) (no) (afterstop) (13:22:00 18:03:26)").getReply
    assert(CommandParser.apply("/start_poll (4)").getReply == "Sorry, cannot start poll if it is active or a start time is defined")
  }

  it should "fail with not existing poll" in {
    assert(CommandParser.apply("/start_poll (100101)").getReply == "Poll does not exist")
  }

  "Stop poll" should "work for usual started poll" in {
    assert(CommandParser.apply("/stop_poll (3)").getReply == "The poll is stopped successfully")
  }

  it should "not stop non-active poll" in {
    assert(CommandParser.apply("/stop_poll (2)").getReply == "Sorry, cannot stop poll if it is not active or a stop time is defined")
  }

  "Begin" should "start the context" in {
    CreatePoll("test_poll", None, None, None, None).getReply
    assert(BeginContext(5).getReply == "Now you are working with context of the poll test_poll")
  }

  "End" should "end the context" in {
    assert(EndContext().getReply == "You stopped working with the poll test_poll")
    assert(selectedPoll == null)
  }

  "Add question" should "work for open question" in {
    BeginContext(5).getReply
    assert(AddQuestion("question", "open", List()).getReply == "Question added successfully")
  }

  it should "work for chice question" in {
    BeginContext(5).getReply
    assert(AddQuestion("question", "chice", List("1", "2")).getReply == "Question added successfully")
  }

  "View" should "show all added  questions" in {
    assert(View().getReply == "")
  }
}
