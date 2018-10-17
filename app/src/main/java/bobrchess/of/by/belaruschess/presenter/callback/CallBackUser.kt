package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBack

/**
 * Created by Igor on 11.04.2018.
 */

interface CallBackUser : CallBack {
    fun onResponse(userDTO: UserDTO)

    fun onResponse(usersDTO: List<UserDTO>)
}
