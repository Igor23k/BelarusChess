package bobrchess.of.by.belaruschess.dto.externalFide

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class WorldTournamentsDataDTO : Serializable {

    @SerializedName("data")
    var data: WorldTournamentsDTO? = null

    constructor(data: WorldTournamentsDTO?) {
        this.data = data
    }
}
