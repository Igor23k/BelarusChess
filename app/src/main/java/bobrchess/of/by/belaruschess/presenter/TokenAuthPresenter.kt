package bobrchess.of.by.belaruschess.presenter

import android.view.View
import bobrchess.of.by.belaruschess.view.activity.AuthorizationContractView

/**
 * Created by Igor on 12.04.2018.
 */

interface TokenAuthPresenter : BasePresenter {
    fun tokenAuthorization()
    fun isAuthenticated(): Boolean
    fun attachView(activity: AuthorizationContractView)
    fun attachViewComponent(view: View)
}
