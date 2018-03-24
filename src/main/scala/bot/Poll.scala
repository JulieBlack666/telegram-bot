package bot

import java.util.Date

class Poll(val name : String, val id : Int, anonymity : Boolean = true,
           continuous_visibility : Boolean = false,
           start_time : Date = null,
           end_time : Date = null) {
  var active = false
  def start(): Unit = active = true
  def stop(): Unit = active = false
  def getResult : String = {
    if (active && !continuous_visibility)
      "Can't see before finished"
    else
      "The poll" + name + "has following result:"
  }

}
