package bot

import scala.io.Source


object Bot {
  def handleCommand(command: String) : String = {
     CommandParser.apply(command).getReply
  }
}
