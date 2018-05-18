package app

import info.mukel.telegrambot4s._, api._, methods._, models._, declarative._
import bot.Bot._


object TelegramIntegration extends TelegramBot with Polling with Commands {
  def token = "566082176:AAGXU23TOXCuvqrCSuD_IYYhfx2SYbw70xg"

  override def receiveMessage(msg: Message): Unit = {
    println(msg)
    for (text <- msg.text) {
      println(msg.source)
      println(msg.from.get.id)
      request(SendMessage(msg.source, handleCommand(text)))
    }
  }

  def main(a: Array[String]): Unit = {
    run()
  }
}