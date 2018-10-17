package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.GameDTO
import bobrchess.of.by.belaruschess.dto.UserDTO

/**
 * Created by Igor on 11.04.2018.
 */

interface CallBackGamesList : CallBack {
    fun onResponse(games: List<GameDTO>)
}
