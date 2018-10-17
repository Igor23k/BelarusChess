package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.TournamentDTO

/**
 * Created by Igor on 11.04.2018.
 */

interface CallBackUserInfo : CallBack {
    fun onResponse(tournaments: List<TournamentDTO>)
}
