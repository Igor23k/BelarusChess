package bobrchess.of.by.belaruschess.dto.externalFide

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TopPlayerImageFile : Serializable {

    @SerializedName("image_file")
    var imageFile: String? = null

    constructor(image_file: String?) {
        this.imageFile = image_file
    }

    constructor()
}
