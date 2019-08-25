package bobrchess.of.by.belaruschess.view.activity

import android.view.View

import com.arellomobile.mvp.MvpView

import bobrchess.of.by.belaruschess.dto.UserDTO

/**
 * Created by Igor on 22.04.2018.
 */

interface RegistrationContractView : MvpView, BaseContractView, ButtonContractView {
    fun registration()
    fun showIncorrectEmailText()
    fun showIncorrectPasswordText()
    fun showSnackBar(componentView: View, text: Int)
    fun showSnackBar(componentView: View, message: String)
    fun startActivity(userDTO: UserDTO)
    fun setGenderSpinnerAdapter(userNames: MutableList<String>)
    fun setCoachSpinnerAdapter(userNames: MutableList<String>)
    fun setRankSpinnerAdapter(rankNames: MutableList<String>)
    fun setCountrySpinnerAdapter(countryNames: MutableList<String>)
    fun loadSpinnersData()
}
