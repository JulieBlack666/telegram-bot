package bot

import scala.io.Source


object Bot {
  def handleCommand(command: String) {
     CommandParser.apply(command).getReply
  }
}
