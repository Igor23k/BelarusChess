package bobrchess.of.by.belaruschess.dto

import java.io.Serializable

class CountryDTO : Serializable{

    var id: Int? = null
    var name: String? = null
    var abbreviation: String? = null
    var image: String? = null

    constructor(id: Int?, name: String, abbreviation: String, image: String) {
        this.id = id
        this.name = name
        this.abbreviation = abbreviation
        this.image = image
    }

    constructor()
}
