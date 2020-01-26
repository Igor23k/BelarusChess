package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.TournamentDTO

/**
 * Created by Igor on 11.04.2018.
 */

interface CallBackTournament : CallBack {
    fun onResponse(tournamentDTO: TournamentDTO)

    fun onResponse(list: List<*>)
}
