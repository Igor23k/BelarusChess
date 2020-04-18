package bobrchess.of.by.belaruschess.dto

import java.io.Serializable

class TournamentResultDTO : Serializable {

    var id: Int? = null
    var name: String? = null
    var position: Int? = null
    var points: Double? = null
    var startDate: String? = null
    var image: String? = null

    constructor() {}

    constructor(name: String?, position: Int?, points: Double?, startDate: String?, image: String?) {
        this.name = name
        this.position = position
        this.points = points
        this.startDate = startDate
        this.image = image
    }
}
