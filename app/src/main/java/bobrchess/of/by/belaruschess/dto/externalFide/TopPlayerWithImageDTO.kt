package bobrchess.of.by.belaruschess.dto.externalFide

import java.io.Serializable

class TopPlayerWithImageDTO : Serializable {

    //todo rename fields
    var id_number: String? = null
    var name: String? = null
    var country: String? = null
    var sex: String? = null
    var birthyear: String? = null
    var title: String? = null
    var standard_rating: String? = null
    var blitz_rating: String? = null
    var rapid_rating: String? = null
    var score: String? = null
    //var image_file: String? = null

    constructor(id_number: String?, name: String?, country: String?, sex: String?, birthyear: String?, title: String?, standard_rating: String?, blitz_rating: String?, rapid_rating: String?, score: String?) {
        this.id_number = id_number
        this.name = name
        this.country = country
        this.sex = sex
        this.birthyear = birthyear
        this.title = title
        this.standard_rating = standard_rating
        this.blitz_rating = blitz_rating
        this.rapid_rating = rapid_rating
        this.score = score
    }

    constructor() {}

}
