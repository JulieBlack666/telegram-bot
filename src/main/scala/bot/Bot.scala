package bot


object Bot {
  def handleCommand(command: String, user : User) : String = {
     CommandParser.apply(command).getReply(user)
  }
}
