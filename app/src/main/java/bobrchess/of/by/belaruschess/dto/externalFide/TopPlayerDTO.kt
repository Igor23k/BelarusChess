package bobrchess.of.by.belaruschess.dto.externalFide

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TopPlayerDTO : Serializable {

    @SerializedName("id_number")
    var id: String? = null
    @SerializedName("top_list")
    var topList: String? = null
    @SerializedName("pos")
    var position: String? = null
    var name: String? = null
    var country: String? = null
    var birthday: String? = null
    var sex: String? = null
    @SerializedName("top_type")
    var topType: String? = null
    var rating: String? = null
    var exp: String? = null
    @SerializedName("period_date")
    var periodDate: String? = null
    @SerializedName("photo")
    var image: String? = null
    @SerializedName("flag_image")
    var flagImage: String? = null
    @SerializedName("blitz_rating")
    var blitzRating: String? = null
    @SerializedName("rapid_rating")
    var rapidRating: String? = null

    constructor(id_number: String?, top_list: String?, pos: String?, name: String?, country: String?, birthday: String?, sex: String?, top_type: String?, rating: String?, exp: String?, period_date: String?, photo: String?, flag_image: String?, blitz_rating: String?, rapid_rating: String?) {
        this.id = id_number
        this.topList = top_list
        this.position = pos
        this.name = name
        this.country = country
        this.birthday = birthday
        this.sex = sex
        this.topType = top_type
        this.rating = rating
        this.exp = exp
        this.periodDate = period_date
        this.image = photo
        this.flagImage = flag_image
        this.blitzRating = blitz_rating
        this.rapidRating = rapid_rating
    }

    constructor() {}

}
