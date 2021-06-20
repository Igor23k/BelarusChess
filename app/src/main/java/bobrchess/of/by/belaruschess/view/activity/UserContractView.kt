package bobrchess.of.by.belaruschess.view.activity

import bobrchess.of.by.belaruschess.dto.UserDTO

interface UserContractView : BaseContractView {
    fun showUsers(users: List<UserDTO>?)
    fun showUser(user: UserDTO?)
}