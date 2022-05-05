package bobrchess.of.by.belaruschess.presenter

import android.view.View
import bobrchess.of.by.belaruschess.dto.extended.ExtendedUserDTO

import bobrchess.of.by.belaruschess.view.activity.RegistrationContractView

interface RegistrationPresenter : BasePresenter {
    fun registration(userDTO: ExtendedUserDTO, userImageUri: String?)
    fun loadCoaches()
    fun loadRanks()
    fun loadCountries()
    fun spinnersAreLoaded(): Boolean
    fun attachView(activity: RegistrationContractView)
    fun attachViewComponent(view: View)
    fun setSelectedRankIndex(selectedRankIndex: Int)
    fun setSelectedCountryIndex(selectedCountryIndex: Int)
    fun setSelectedCoachIndex(selectedCoachIndex: Int)
    fun setSelectedGenderIndex(selectedGenderIndex: Int)
    fun loadSpinnersData()
}