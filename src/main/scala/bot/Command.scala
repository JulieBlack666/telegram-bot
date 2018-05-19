package bot

trait Command {
  def getReply(user: User): String
}
