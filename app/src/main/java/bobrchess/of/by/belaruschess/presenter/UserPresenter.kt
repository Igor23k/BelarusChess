package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.view.activity.UserContractView

/**
 * Created by Igor on 12.04.2018.
 */

interface UserPresenter : BasePresenter {
    fun loadUserById(id: Int)

    fun loadReferees()

    fun loadUsers()

    fun loadUsers(count: Int?)

    fun searchUsers(text: String)

    fun attachView(userContractView: UserContractView)
}
