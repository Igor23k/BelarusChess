package bobrchess.of.by.belaruschess.pojo

import java.util.*

/**
 * Created by Igor on 16.03.2018.
 */
data class Match(val countPointsFirstTeam: Float, val countPointsSecondTeam: Float, val tournament: Tournament, val firstTeam: TournamentTeam, val secondTeam: TournamentTeam, val date: Date)