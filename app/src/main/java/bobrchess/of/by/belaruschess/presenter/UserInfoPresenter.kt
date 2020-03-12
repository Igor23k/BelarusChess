package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.outdated.MainOldActivity

/**
 * Created by Igor on 12.04.2018.
 */

interface UserInfoPresenter : BasePresenter {
    fun attachView(activity: MainOldActivity)

    fun loadUserTournaments()
}
