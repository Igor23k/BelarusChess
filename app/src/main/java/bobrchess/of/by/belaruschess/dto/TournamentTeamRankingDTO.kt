package bobrchess.of.by.belaruschess.dto

import java.io.Serializable

/**
 * Created by Igor on 10.04.2018.
 */

class TournamentTeamRankingDTO : Serializable {

    var id: Int? = null
    var position: Int? = null
    var points: Double? = null
    var team: TeamDTO? = null
    var tournament: TournamentDTO? = null

    constructor(id: Int?, position: Int?, points: Double?, team: TeamDTO?, tournamet: TournamentDTO?) {
        this.id = id
        this.position = position
        this.points = points
        this.team = team
        this.tournament = tournamet
    }

    constructor() {}
}
