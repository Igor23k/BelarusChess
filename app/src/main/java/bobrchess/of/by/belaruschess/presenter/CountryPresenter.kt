package bobrchess.of.by.belaruschess.presenter

import bobrchess.of.by.belaruschess.view.activity.impl.AuthorizationActivity

/**
 * Created by Igor on 12.04.2018.
 */

interface CountryPresenter : BasePresenter {
    fun getCountry(id: Int?)

    fun attachView(activity: AuthorizationActivity)
}