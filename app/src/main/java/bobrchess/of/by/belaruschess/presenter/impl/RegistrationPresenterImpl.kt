package bobrchess.of.by.belaruschess.presenter.impl

import android.support.annotation.NonNull
import android.view.View
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.CountryDTO
import bobrchess.of.by.belaruschess.dto.RankDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.exception.IncorrectDataException
import bobrchess.of.by.belaruschess.network.connection.RegistrationConnection
import bobrchess.of.by.belaruschess.presenter.RegistrationPresenter
import bobrchess.of.by.belaruschess.presenter.callback.CallBackRegistration
import bobrchess.of.by.belaruschess.util.Constants.SERVER_UNAVAILABLE
import bobrchess.of.by.belaruschess.util.Constants.UNSUCCESSFUL_REQUEST
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.util.Util.TYPE_NOT_CONNECTED
import bobrchess.of.by.belaruschess.util.Validator
import bobrchess.of.by.belaruschess.view.activity.RegistrationContractView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import java.util.*


@InjectViewState
class RegistrationPresenterImpl : MvpPresenter<RegistrationContractView>, CallBackRegistration, RegistrationPresenter {

    private var view: RegistrationContractView? = null
    private var viewComponent: View? = null
    private var userConnection: RegistrationConnection = RegistrationConnection()
    private var viewIsReady: Boolean? = false
    private var selectedRankIndex: Int = 0
    private var selectedCountryIndex: Int = 0
    private var selectedCoachIndex: Int = 0
    private var selectedGenderIndex: Int = 0
    private val ranksIndexes = HashMap<Int, RankDTO>()
    private val countriesIndexes = HashMap<Int, CountryDTO>()
    private val coachesIndexes = HashMap<Int, UserDTO>()
    private var connectivityStatus: Int? = null
    private var firstTimeSpinnerLoad: Boolean = true

    companion object {
        private const val NOT_SELECTED_INDEX = 0
    }

    constructor() {
        userConnection.attachPresenter(this)
    }

    override fun onResponse(userDTO: UserDTO) {
        view!!.hideProgress()
        view!!.enableButton()
        view!!.startActivity(userDTO)
    }

    override fun onCoachResponse(coaches: List<UserDTO>) {
        saveCoachesIndexes(coaches)
        view!!.setCoachSpinnerAdapter(Util.getUsersNames(coaches))
        loadRanks()
    }

    override fun onRankResponse(ranks: List<RankDTO>) {
        saveRanksIndexes(ranks)
        view!!.setRankSpinnerAdapter(Util.getRanksNames(ranks))
        loadCountries()
    }

    override fun onCountryResponse(countries: List<CountryDTO>) {
        saveCountriesIndexes(countries)
        view!!.setCountrySpinnerAdapter(Util.getCountriesNames(countries))
        viewIsReady = true
        view!!.hideProgress()
    }

    override fun onFailure(t: Throwable) {
        view!!.hideProgress()
        view!!.enableButton()
        when (t.message) {
            SERVER_UNAVAILABLE -> {
                onServerUnavailable()
            }
            UNSUCCESSFUL_REQUEST -> {
                onUnsuccessfulRequest()
            }
        }
    }

    private fun onUnsuccessfulRequest() {
        view!!.showSnackBar(viewComponent!!, R.string.internalServerError, R.string.retry)
    }

    private fun onServerUnavailable() {
        when {
            connectivityStatus == TYPE_NOT_CONNECTED -> view!!.showNoConnectionAlertDialog(R.string.noInternetConnection, R.string.noInternetConnectionMessage, R.string.retry, false)
            !viewIsReady!! -> view!!.showServerInUnavailableAlertDialog(R.string.serverIsUnavailable, R.string.serverIsUnavailableMessage, R.string.retry, false)
            else -> view!!.showSnackBar(viewComponent!!, R.string.serverIsUnavailable, R.string.retry)
        }
    }

    override fun loadSpinnersData() {
        view!!.showProgress()
        loadCoaches()
    }

    override fun registration(userDTO: UserDTO) {
        view!!.disableButton()
        setUserData(userDTO)
        try {
            Validator.validateUserData(userDTO)
            userDTO.password = Util.getEncodedPassword(userDTO.password)
            view!!.showProgress()
            userConnection.registration(userDTO)
        } catch (e: IncorrectDataException) {
            view!!.enableButton()
            view!!.showSnackBar(viewComponent!!, e.localizedMessage, R.string.retry)
        }
    }

    private fun setUserData(@NonNull userDTO: UserDTO) {
        if (selectedRankIndex !== NOT_SELECTED_INDEX) {// bug equals?
            userDTO.rank = ranksIndexes[selectedRankIndex.minus(2)]
        }
        if (selectedCountryIndex !== NOT_SELECTED_INDEX) {
            userDTO.country = countriesIndexes[selectedCountryIndex.minus(1)]
        }
        if (selectedCoachIndex !== NOT_SELECTED_INDEX) {
            userDTO.coach = coachesIndexes[selectedCoachIndex.minus(2)]
        }
        if (selectedCoachIndex !== NOT_SELECTED_INDEX) {
            userDTO.beMale = isMale(selectedGenderIndex - 1)
        }
        userDTO.beCoach = true// bug думать, думать
        userDTO.beOrganizer = true
    }

    override fun setConnectivityStatus(connectivityStatus: Int?) {
        this.connectivityStatus = connectivityStatus
        if (firstTimeSpinnerLoad) {
            firstTimeSpinnerLoad = false
            if (connectivityStatus == TYPE_NOT_CONNECTED) {
                view!!.showNoConnectionAlertDialog(R.string.noInternetConnection, R.string.noInternetConnectionMessage, R.string.retry, false)
            } else if ((!viewIsReady!!)) {
                loadSpinnersData()
            }
        }
    }

    override fun loadCoaches() {
        userConnection.getCoaches()
    }

    override fun loadRanks() {
        userConnection.getRanks()
    }

    override fun loadCountries() {
        userConnection.getCountries()
    }

    override fun attachView(activity: RegistrationContractView) {
        view = activity
    }

    override fun attachViewComponent(viewComponent: View) {
        this.viewComponent = viewComponent
    }

    override fun detachView() {
        view = null
    }

    override fun viewIsReady() {
        viewIsReady = true
        view?.hideProgress()
        view?.enableButton()
    }

    override fun setSelectedRankIndex(selectedRankIndex: Int) {
        this.selectedRankIndex = selectedRankIndex
    }

    override fun setSelectedCountryIndex(selectedCountryIndex: Int) {
        this.selectedCountryIndex = selectedCountryIndex
    }

    override fun setSelectedCoachIndex(selectedCoachIndex: Int) {
        this.selectedCoachIndex = selectedCoachIndex
    }

    override fun setSelectedGenderIndex(selectedGenderIndex: Int) {
        this.selectedGenderIndex = selectedGenderIndex
    }

    private fun saveRanksIndexes(ranks: List<RankDTO>) {
        for (i in ranks.indices) {
            ranksIndexes[i] = ranks[i]
        }
    }

    private fun saveCountriesIndexes(countries: List<CountryDTO>) {
        for (i in countries.indices) {
            countriesIndexes[i] = countries[i]
        }
    }

    private fun saveCoachesIndexes(coaches: List<UserDTO>) {
        for (i in coaches.indices) {
            coachesIndexes[i] = coaches[i]
        }
    }

    private fun isMale(index: Int?): Boolean {
        return index == 0
    }
}
