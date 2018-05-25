package bot_tests

import bot.CommandParser
import bot.Commands._
import bot.ContextCommands._
import org.scalatest.FlatSpec


class TestParser extends FlatSpec {
  "List Polls" should "be parsed" in {
    assert(CommandParser.apply("/list") == ListPolls())
  }

  "Bad Request" should "not be parsed" in {
    assert(CommandParser.apply("/bad_command") == BadRequest())
  }

  "Create poll" should "be parsed just with name" in {
    assert(CommandParser.apply("/create_poll (poll_name)") == CreatePoll("poll_name", None, None, None, None))
    assert(CommandParser.apply("/create_poll  (poll_name)") == CreatePoll("poll_name", None, None, None, None))
  }
  it should "be parsed with anonymity" in {
    assert(CommandParser.apply("/create_poll (poll_name) (yes)") == CreatePoll("poll_name", Option("yes"), None, None, None))
    assert(CommandParser.apply("/create_poll (poll_name) (no)") == CreatePoll("poll_name", Option("no"), None, None, None))
    assert(CommandParser.apply("/create_poll  (poll_name)  (no)") == CreatePoll("poll_name", Option("no"), None, None, None))
  }
  it should "be parsed with bad parameter" in {
    assert(CommandParser.apply("/create_poll (poll_name) (bad_parameter)") == BadRequest())
  }
  it should "be parsed with visability modifier" in {
    assert(CommandParser.apply("/create_poll (poll_name) (yes) (afterstop)") == CreatePoll("poll_name", Option("yes"),
      Option("afterstop"), None, None))
    assert(CommandParser.apply("/create_poll (poll_name) (no) (continuous)") == CreatePoll("poll_name", Option("no"),
      Option("continuous"), None, None))
  }
  it should "be parsed with start and end time" in {
    assert(CommandParser.apply("/create_poll (poll_name) (yes) (afterstop) (13:22:00 18:03:26) (15:22:00 18:03:26)") ==
      CreatePoll("poll_name", Option("yes"),
        Option("afterstop"), Option("13:22:00 18:03:26"), Option("15:22:00 18:03:26")))
  }

  "Delete poll" should "be parsed" in {
    assert(CommandParser.apply("/delete_poll (1)") == DeletePoll(1))
  }
  it should "be parsed with redundant spaces" in {
    assert(CommandParser.apply("/delete_poll    (42)   ") == DeletePoll(42))
  }
  it should "not be parsed with bad parameter" in {
    assert(CommandParser.apply("/delete_poll (ololo)") == BadRequest())
  }

  "Start poll" should "be parsed" in {
    assert(CommandParser.apply("/start_poll (2)") == StartPoll(2))
  }

  "Stop poll" should "be parsed" in {
    assert(CommandParser.apply("/stop_poll (1)") == StopPoll(1))
  }

  "Result" should "be parsed" in {
    assert(CommandParser.apply("/result (5)") == PollResult(5))
  }

  "Begin" should "be parsed" in {
    assert(CommandParser.apply("/begin (5)") == BeginContext(5))
  }

  "End" should "be parsed" in {
    assert(CommandParser.apply("/end") == EndContext())
  }

  "Question" should "be parsed open" in {
    assert(CommandParser.apply("/add_question (doctor who?) (open)") ==
      AddQuestion("doctor who?", "open", List()))
  }

  it should "be parsed choice" in {
    assert(CommandParser.apply(
      "/add_question (doctor who?) (choice) \n (meow) \n (nobody)") ==
      AddQuestion("doctor who?", "choice", List("meow", "nobody")))
  }

  it should "be parsed multi" in {
    assert(CommandParser.apply(
      "/add_question (doctor who?) (multi) \n (meow) \n (nobody)") ==
      AddQuestion("doctor who?", "multi", List("meow", "nobody")))
  }

  "View" should "be parsed" in {
    assert(CommandParser.apply("/view") == View())
  }

  "Answer question" should "be parsed open" in {
    assert(CommandParser.apply("/answer (0) (hello)") == AnswerQuestionOpen(0, "hello"))
  }

  it should "be parsed choice" in {
    assert(CommandParser.apply("/answer (0) (1)") == AnswerQuestionChoiceMulti(0, List("1")))
  }

  it should "be parsed multi" in {
    assert(CommandParser.apply("/answer (0) (1 2 3)") == AnswerQuestionChoiceMulti(0, List("1", "2", "3")))
  }

  "Delete question" should "be parsed" in {
    assert(CommandParser.apply("/delete_question (0)") == DeleteQuestion(0))
  }

  "Double parentheses" should "be parsed into single create_poll" in {
    assert(CommandParser.apply("/create_poll (name with ((parentheses)) in the middle)")
      == CreatePoll("name with (parentheses) in the middle", None, None, None, None))
    assert(CommandParser.apply("/create_poll (name with ((parentheses in the end)))")
      == CreatePoll("name with (parentheses in the end)", None, None, None, None))
  }
  it should "be parsed into single add_question" in {
  assert(CommandParser.apply("/add_question (((parentheses)) here) (choice) \n (and((here)))")
    == AddQuestion("(parentheses) here", "choice", List("and(here)")))
  }
  it should "be parsed into single answer" in {
    assert(CommandParser.apply("/answer (0) (parentheses ((in)) answer)")
      == AnswerQuestionOpen(0, "parentheses (in) answer"))
  }
}
