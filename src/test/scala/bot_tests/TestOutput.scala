package bot_tests

import bot.{CommandParser, User}
import bot.ContextCommands._
import bot.Commands._
import org.scalatest.FlatSpec

class TestOutput extends FlatSpec {
  val test_user = new User("test_user", 1)
  "List polls" should "be empty" in {
    assert(CommandParser.apply("/list").getReply(test_user) == "You have no polls")
  }

  "Create bad poll" should "be bad request" in {
    assert(CommandParser.apply("/create_poll").getReply(test_user) == "Bad request")
  }

  "List polls" should "be empty after bad request" in {
    assert(CommandParser.apply("/list").getReply(test_user) == "You have no polls")
  }

  "Create poll" should "return id" in {
    assert(CommandParser.apply("/create_poll (poll_name)").getReply(test_user) forall Character.isDigit)
  }

  "List polls" should "now be with poll" in {
    assert(CommandParser.apply("/list").getReply(test_user) == "Current polls:\n  0 : poll_name")
  }

  "Delete poll" should "actually delete poll" in {
    assert(CommandParser.apply("/delete_poll (0)").getReply(test_user) == "Poll deleted successfully")
    assert(CommandParser.apply("/list").getReply(test_user) == "You have no polls")
  }

  "Result" should "not be seen if poll is started and afterstop" in {
    CommandParser.apply("/create_poll (afterstop_poll) (yes) (afterstop)").getReply(test_user)
    assert(CommandParser.apply("/list").getReply(test_user) == "Current polls:\n  1 : afterstop_poll")
    assert(CommandParser.apply("/start_poll (1)").getReply(test_user) == "The poll is started successfully")
    assert(CommandParser.apply("/result (1)").getReply(test_user) == "Can't see before finished")
  }

  it should "give a result if poll is stopped" in {
    CommandParser.apply("/stop_poll (1)").getReply(test_user)
    assert(CommandParser.apply("/result (1)").getReply(test_user) == "There is no questions yet")
  }

  it should "give results for continuous poll in any situation" in {
    CommandParser.apply("/create_poll (continuous_poll) (yes) (continuous)").getReply(test_user)
    assert(CommandParser.apply("/result (2)").getReply(test_user) == "There is no questions yet")
    CommandParser.apply("/start_poll (2)").getReply(test_user)
    assert(CommandParser.apply("/result (2)").getReply(test_user) == "There is no questions yet")
    CommandParser.apply("/stop_poll (2)").getReply(test_user)
    assert(CommandParser.apply("/result (2)").getReply(test_user) == "There is no questions yet")
  }

  "Start poll" should "work for usual poll" in {
    CommandParser.apply("/create_poll (usual_poll)").getReply(test_user)
    assert(CommandParser.apply("/start_poll (3)").getReply(test_user) == "The poll is started successfully")
  }

  it should "not work for poll with defined start time" in {
    CommandParser.apply("/create_poll (poll_with_defined_start_time) (no) (afterstop) (13:22:00 18:03:26)").getReply(test_user)
    assert(CommandParser.apply("/start_poll (4)").getReply(test_user) == "Sorry, cannot start poll if it is active or a start time is defined")
  }

  it should "fail with not existing poll" in {
    assert(CommandParser.apply("/start_poll (100101)").getReply(test_user) == "Poll does not exist")
  }

  "Stop poll" should "work for usual started poll" in {
    assert(CommandParser.apply("/stop_poll (3)").getReply(test_user) == "The poll is stopped successfully")
  }

  it should "not stop non-active poll" in {
    assert(CommandParser.apply("/stop_poll (2)").getReply(test_user) == "Sorry, cannot stop poll if it is not active or a stop time is defined")
  }

  "Begin" should "start the context" in {
    CreatePoll("test_poll", None, None, None, None).getReply(test_user)
    assert(BeginContext(5).getReply(test_user) == "Now you are working with context of the poll test_poll")
  }

  "End" should "end the context" in {
    assert(EndContext().getReply(test_user) == "You stopped working with the poll test_poll")
    assert(selectedPoll == null)
  }

  "Add question" should "work for open question" in {
    BeginContext(5).getReply(test_user)
    assert(AddQuestion("question1", "open", List()).getReply(test_user) == "Question added successfully")
  }

  it should "work for chice question" in {
    BeginContext(5).getReply(test_user)
    assert(AddQuestion("question2", "choice", List("1", "2")).getReply(test_user) == "Question added successfully")
  }

  "View" should "show all added  questions" in {
    assert(View().getReply(test_user) ==
      """Poll: test_poll id: 5
        |start time: null end time: null
        |is not active
        |poll is anonymous
        |Question: question1
        |type: open
        |
        |  Question: question2
        |type: choice
        |variants:
        |0) 1: 0
        |1) 2: 0
        |
      """.stripMargin)
  }
}
