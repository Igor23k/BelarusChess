package bobrchess.of.by.belaruschess.dto.externalFide

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TopPlayerWithImageDTO : Serializable {

    @SerializedName("id_number")
    var idNumber: String? = null
    var name: String? = null
    var country: String? = null
    var sex: String? = null
    var birthyear: String? = null
    var title: String? = null
    @SerializedName("standard_rating")
    var standardRating: String? = null
    @SerializedName("blitz_rating")
    var blitzRating: String? = null
    @SerializedName("rapid_rating")
    var rapidRating: String? = null
    var score: String? = null
    @SerializedName("image_file")
    var imageFile: TopPlayerImageFile? = null

    constructor(id_number: String?, name: String?, country: String?, sex: String?, birthyear: String?, title: String?, standard_rating: String?, blitz_rating: String?, rapid_rating: String?, score: String?, image_file: TopPlayerImageFile?) {
        this.idNumber = id_number
        this.name = name
        this.country = country
        this.sex = sex
        this.birthyear = birthyear
        this.title = title
        this.standardRating = standard_rating
        this.blitzRating = blitz_rating
        this.rapidRating = rapid_rating
        this.score = score
        this.imageFile = image_file
    }

    constructor() {}
}
