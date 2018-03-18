package bot

import java.time.LocalDateTime

class Poll(_name : String, _id : Int, _anonymity : Boolean = true,
           continuous_visibility : Boolean = false,
           start_time : LocalDateTime = null,
           end_time : LocalDateTime = null) {
  val name : String = _name
  val id : Int = _id
  val anonymity : Boolean = _anonymity
}
