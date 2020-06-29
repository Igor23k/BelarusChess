package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.TournamentResultDTO

interface CallBackUserInfo : CallBack {
    fun onResponse(tournamentsResults: List<TournamentResultDTO>)
}
