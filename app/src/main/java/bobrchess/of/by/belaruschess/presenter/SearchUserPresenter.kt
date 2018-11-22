package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.view.activity.impl.SearchUserActivity

/**
 * Created by Igor on 12.04.2018.
 */

interface SearchUserPresenter : BasePresenter {
    fun loadUsers()

    fun loadUsers(count: Int?)

    fun searchUsers()

    fun attachView(activity: SearchUserActivity)
}
