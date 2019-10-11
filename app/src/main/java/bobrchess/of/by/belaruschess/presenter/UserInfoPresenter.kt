package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.view.activity.impl.MainActivity

/**
 * Created by Igor on 12.04.2018.
 */

interface UserInfoPresenter : BasePresenter {
    fun attachView(activity: MainActivity)

    fun loadUserTournaments()
}
