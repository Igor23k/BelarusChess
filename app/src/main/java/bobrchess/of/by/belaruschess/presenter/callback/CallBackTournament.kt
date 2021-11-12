package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.TournamentDTO

interface CallBackTournament : CallBack {
    fun onResponse(tournamentDTO: TournamentDTO)

    fun onResponse(list: List<*>)
}
