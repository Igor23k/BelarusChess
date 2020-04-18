package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.TournamentResultDTO

/**
 * Created by Igor on 11.04.2018.
 */

interface CallBackUserInfo : CallBack {
    fun onResponse(tournamentsResults: List<TournamentResultDTO>)
}
