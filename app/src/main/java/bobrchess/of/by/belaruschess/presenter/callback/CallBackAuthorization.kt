package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.UserContextDTO
import bobrchess.of.by.belaruschess.dto.UserDTO

interface CallBackAuthorization : CallBack {
    fun onResponse(userContextDTO: UserContextDTO)
    fun onResponse(userDTO: UserDTO)
}
