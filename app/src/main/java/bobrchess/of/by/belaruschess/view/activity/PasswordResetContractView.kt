package bobrchess.of.by.belaruschess.view.activity

import android.view.View
import com.arellomobile.mvp.MvpView

interface PasswordResetContractView : MvpView, BaseContractView, ButtonContractView {
    fun showIncorrectEmailText()
    fun showSnackBar(componentView: View, message: String, buttonText: Int?)
    fun showSnackBar(componentView: View, message: Int?, buttonText: Int?)
    fun showSnackBar(componentView: View, text: Int)
    fun showSnackBar(componentView: View, message: String)
    fun initActivityData()
}
