package bobrchess.of.by.belaruschess.dto

import java.io.Serializable
import java.util.Date

/**
 * Created by Igor on 10.04.2018.
 */

class MatchDTO : Serializable {
    var id: Int? = null

    var countPointsFirstTeam: Double? = null

    var countPointsSecondTeam: Double? = null

    var date: Date? = null

    var tournament: TournamentDTO? = null

    var firstTeam: TeamDTO? = null

    var secondTeam: TeamDTO? = null

    constructor(id: Int?, countPointsFirstTeam: Double?, countPointsSecondTeam: Double?, date: Date, tournamentDTO: TournamentDTO, firstTeam: TeamDTO, secondTeam: TeamDTO) {
        this.id = id
        this.countPointsFirstTeam = countPointsFirstTeam
        this.countPointsSecondTeam = countPointsSecondTeam
        this.date = date
        this.tournament = tournamentDTO
        this.firstTeam = firstTeam
        this.secondTeam = secondTeam
    }

    constructor() {}
}
