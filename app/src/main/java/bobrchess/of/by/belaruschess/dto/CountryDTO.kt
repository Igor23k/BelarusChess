package bobrchess.of.by.belaruschess.dto

/**
 * Created by Igor on 10.04.2018.
 */

class CountryDTO {
    var id: Int? = null
    var name: String? = null
    var abbreviation: String? = null

    constructor(id: Int?, name: String, abbreviation: String) {
        this.id = id
        this.name = name
        this.abbreviation = abbreviation
    }

    constructor() {}
}
