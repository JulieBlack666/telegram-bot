package bot

import info.mukel.telegrambot4s.models.User

import scala.io.Source


object Bot {
  def handleCommand(command: String, user : User) : String = {
     CommandParser.apply(command).getReply(user)
  }
}
