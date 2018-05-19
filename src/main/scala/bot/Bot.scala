package bot

import info.mukel.telegrambot4s.models.User

import scala.io.Source


object Bot {
  def handleCommand(command: String) : String = {
     CommandParser.apply(command).getReply(new User("test", 1))
  }
}
