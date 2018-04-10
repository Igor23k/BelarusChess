package bobrchess.of.by.belaruschess.dto

/**
 * Created by Igor on 10.04.2018.
 */

class TournamentTeamDTO {
    var id: Int? = null

    var name: String? = null

    constructor(id: Int?, name: String) {
        this.id = id
        this.name = name
    }

    constructor() {}
}
