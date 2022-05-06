package bobrchess.of.by.belaruschess.view.activity

import android.view.View

import com.arellomobile.mvp.MvpView

import bobrchess.of.by.belaruschess.dto.UserDTO

interface PasswordResetContractView : MvpView, BaseContractView, ButtonContractView {
    fun unsuccessfulTokenAuth()
    fun showIncorrectEmailText()
    fun showIncorrectPasswordText()
    fun startActivity(userDTO: UserDTO)
    fun showSnackBar(componentView: View, message: String, buttonText: Int?)
    fun showSnackBar(componentView: View, message: Int?, buttonText: Int?)
    fun showSnackBar(componentView: View, text: Int)
    fun showSnackBar(componentView: View, message: String)
    fun tokenAuthorization()
    fun initActivityData()
}
