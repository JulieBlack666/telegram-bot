package bot

import java.time.LocalDateTime

class Poll(_name : String, _anonymity : Boolean = true, _continuous_visibility : Boolean = false,
          start_time : LocalDateTime = null, end_time : LocalDateTime = null) {
  val name : String = _name
  val id : Int = 123
  val anonymity : Boolean = _anonymity
}
