package bobrchess.of.by.belaruschess.dto.externalFide

import java.io.Serializable

class TopPlayersDTO : Serializable {

    var open: List<TopPlayerDTO>? = null
    var girls: List<TopPlayerDTO>? = null
    var juniors: List<TopPlayerDTO>? = null
    var women: List<TopPlayerDTO>? = null

}
