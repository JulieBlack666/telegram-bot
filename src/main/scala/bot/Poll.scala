package bot

import java.time.LocalDateTime
import java.util.Date

class Poll(val name : String, val id : Int, anonymity : Boolean = true,
           continuous_visibility : Boolean = false,
           start_time : Date = null,
           end_time : Date = null) {

}
