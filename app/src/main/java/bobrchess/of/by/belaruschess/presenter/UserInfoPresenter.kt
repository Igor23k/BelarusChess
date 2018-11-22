package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.view.activity.impl.UserInfoActivity

/**
 * Created by Igor on 12.04.2018.
 */

interface UserInfoPresenter : BasePresenter {
    fun attachView(activity: UserInfoActivity)

    fun loadUserTournaments()
}
