package bot

import bot.Commands._polls

object ContextCommands {

  var selectedPoll : Poll = _

  case class BeginContext(id : Int) extends Command {
    override def getReply: String = {
      "сообщение об успехе или ошибка"
    }
  }

  case class EndContext() extends Command {
    override def getReply: String = {
      "сообщение об успехе или ошибка"
    }
  }

  case class View() extends Command {
    override def getReply: String = {
      "Красивое представление опроса"
    }
  }
}
