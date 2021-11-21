package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.dto.UserDTO

interface CallBackSearchTournament : CallBack {
    fun onResponse(tournamentDTO: List<TournamentDTO>)
}
