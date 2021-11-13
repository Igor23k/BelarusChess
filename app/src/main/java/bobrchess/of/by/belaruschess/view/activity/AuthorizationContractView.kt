package bobrchess.of.by.belaruschess.view.activity

import android.view.View

import com.arellomobile.mvp.MvpView

import bobrchess.of.by.belaruschess.dto.UserDTO

/**
 * Created by Igor on 22.04.2018.
 */

interface AuthorizationContractView : MvpView, BaseContractView, ButtonContractView {
    fun unsuccessfulTokenAuth()
    fun showIncorrectEmailText()
    fun showIncorrectPasswordText()
    fun startActivity(userDTO: UserDTO)
    fun showSnackBar(componentView: View, message: String, buttonText: Int?)
    fun showSnackBar(componentView: View, message: Int?, buttonText: Int?)
    fun showSnackBar(componentView: View, text: Int)
    fun showSnackBar(componentView: View, message: String)
    fun authorization()
    fun tokenAuthorization()
    fun initActivityData()
}
