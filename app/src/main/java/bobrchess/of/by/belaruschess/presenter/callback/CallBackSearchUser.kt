package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.TournamentDTO
import bobrchess.of.by.belaruschess.dto.UserDTO

/**
 * Created by Igor on 11.04.2018.
 */

interface CallBackSearchUser : CallBack {
    fun onResponse(users: List<UserDTO>)
}
