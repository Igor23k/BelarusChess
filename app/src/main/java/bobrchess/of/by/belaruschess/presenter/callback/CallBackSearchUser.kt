package bobrchess.of.by.belaruschess.presenter.callback

import bobrchess.of.by.belaruschess.dto.UserDTO

interface CallBackSearchUser : CallBack {
    fun onResponse(users: List<UserDTO>)
    fun onResponse(user: UserDTO)
}
