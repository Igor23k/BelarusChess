package bobrchess.of.by.belaruschess.dto

import java.util.Date

/**
 * Created by Igor on 10.04.2018.
 */

class MatchDTO {
    var id: Int? = null

    var countPointsFirstTeam: Double? = null

    var countPointsSecondTeam: Double? = null

    var date: Date? = null

    var tournament: TournamentDTO? = null

    var firstTeam: TournamentTeamDTO? = null

    var secondTeam: TournamentTeamDTO? = null

    constructor(id: Int?, countPointsFirstTeam: Double?, countPointsSecondTeam: Double?, date: Date, tournamentDTO: TournamentDTO, firstTeam: TournamentTeamDTO, secondTeam: TournamentTeamDTO) {
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
