package bobrchess.of.by.belaruschess.dto.externalFide

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class WorldTournamentDataDTO : Serializable {

    @SerializedName("data")
    var data: WorldTournamentDTO? = null

    constructor(data: WorldTournamentDTO?) {
        this.data = data
    }
}
