package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.GameDTO
import bobrchess.of.by.belaruschess.dto.TournamentDTO

/**
 * Created by Igor on 11.04.2018.
 */

interface CallBackGame : CallBack {
    fun onResponse(gameDTO: GameDTO)

    fun onResponse(list: List<GameDTO>)
}
