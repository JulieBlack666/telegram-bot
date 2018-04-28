package bot

import java.util.{Calendar, Date}

class Poll(val name : String, val id : Int, anonymity : Boolean = true,
           continuous_visibility : Boolean = false,
           start_time : Date = null,
           end_time : Date = null) {
  // .copy case classes
  // pattern matching flag => ...

  var active = false
  def start(): String = {
    if (start_time != null)
      "Cannot start if start time is defined"
    else if(active)
      "Cannot start active poll"
    else {
      active = true
      "The poll is started successfully"
    }
  }
  def stop(): String = {
    if (end_time != null)
      "Cannot stop if start time is defined"
    else if (!active)
      "Cannot stop poll before it started"
    else {
      active = false
      "The poll is stopped successfully"
    }
  }
  def getResult : String = {
    if (active && !continuous_visibility)
      "Can't see before finished"
    else
      "The poll " + name + " has following result:"
  }

}
