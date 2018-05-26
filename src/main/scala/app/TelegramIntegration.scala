package app

import info.mukel.telegrambot4s._
import api._
import methods._
import models._
import declarative._
import bot.Bot._
import bot.User


object TelegramIntegration extends TelegramBot with Polling with Commands {
  lazy val token = "566082176:AAGXU23TOXCuvqrCSuD_IYYhfx2SYbw70xg"

  override def receiveMessage(msg: Message): Unit = {
    for (text <- msg.text) {
      request(SendMessage(msg.source, handleCommand(text, User(msg.from.get.firstName, msg.chat.id))))
    }
  }

  def main(a: Array[String]): Unit = {
    run()
  }
}