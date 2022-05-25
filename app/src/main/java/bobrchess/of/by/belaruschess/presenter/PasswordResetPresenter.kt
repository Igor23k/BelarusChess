package bobrchess.of.by.belaruschess.presenter

import android.view.View
import bobrchess.of.by.belaruschess.view.activity.PasswordResetContractView

interface PasswordResetPresenter : BasePresenter {
    fun reset(userEmail: String)
    fun attachView(activity: PasswordResetContractView)
    fun attachViewComponent(view: View)
    fun onEmailNotFound()
    fun onNewPasswordSent()
}
