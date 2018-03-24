package bot

import java.time.LocalDateTime

class Poll(val name : String, val id : Int, anonymity : Boolean = true,
           continuous_visibility : Boolean = false,
           start_time : LocalDateTime = null,
           end_time : LocalDateTime = null) {

}
