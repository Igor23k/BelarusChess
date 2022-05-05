package bobrchess.of.by.belaruschess.presenter.impl

import android.support.annotation.NonNull
import android.view.View
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.*
import bobrchess.of.by.belaruschess.dto.extended.ExtendedUserDTO
import bobrchess.of.by.belaruschess.network.connection.internal.RegistrationConnection
import bobrchess.of.by.belaruschess.presenter.RegistrationPresenter
import bobrchess.of.by.belaruschess.presenter.callback.CallBackRegistration
import bobrchess.of.by.belaruschess.util.Constants
import bobrchess.of.by.belaruschess.util.Constants.Companion.ABSENCE_INDEX
import bobrchess.of.by.belaruschess.util.Constants.Companion.INTERNAL_SERVER_ERROR
import bobrchess.of.by.belaruschess.util.Constants.Companion.KEY_SERVER_UNAVAILABLE
import bobrchess.of.by.belaruschess.util.Constants.Companion.NOT_SELECTED_INDEX
import bobrchess.of.by.belaruschess.util.Constants.Companion.SERVER_UNAVAILABLE
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.util.Util.Companion.TYPE_NOT_CONNECTED
import bobrchess.of.by.belaruschess.util.Util.Companion.compressImage
import bobrchess.of.by.belaruschess.util.Util.Companion.getInternalizedMessage
import bobrchess.of.by.belaruschess.util.Validator
import bobrchess.of.by.belaruschess.view.activity.PackageModel
import bobrchess.of.by.belaruschess.view.activity.RegistrationContractView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.springframework.util.CollectionUtils
import org.springframework.util.StringUtils


@InjectViewState
class RegistrationPresenterImpl : MvpPresenter<RegistrationContractView>(), CallBackRegistration, RegistrationPresenter {

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
    private val coachesIndexes = HashMap<Int, String>()
    private var connectivityStatus: Int? = null
    private var firstTimeSpinnerLoad: Boolean = true
    private var packageModel: PackageModel? = null

    init {
        userConnection.attachPresenter(this)
    }

    fun setPackageModel(packageModel: PackageModel) {
        this.packageModel = packageModel
    }

    override fun onResponse(userContextDTO: UserContextDTO) {
        packageModel?.putTokenMap(userContextDTO.tokenMap)
        view!!.hideProgress()
        view!!.enableButton()
        view!!.startActivity(userContextDTO.user)
    }

    override fun onCoachResponse(coaches: MutableList<UserDTO>) {
        val coachesNames = Util.getUsersBasicData(coaches).toMutableList();
        saveCoachesIndexes(coachesNames)
        view!!.setCoachSpinnerAdapter(coachesNames)
        loadRanks()
    }

    override fun onRankResponse(ranks: MutableList<RankDTO>) {
        saveRanksIndexes(ranks)
        view!!.setRankSpinnerAdapter(Util.getRanksNames(ranks).toMutableList())
        loadCountries()
    }

    override fun onCountryResponse(countries: MutableList<CountryDTO>) {
        saveCountriesIndexes(countries)
        view!!.setCountrySpinnerAdapter(Util.getCountriesNames(countries))
        viewIsReady = true
        view!!.hideProgress()
    }

    override fun onFailure(errorDTO: ErrorDTO) {
        view!!.hideProgress()
        view!!.enableButton()
        when (errorDTO.error) {
            SERVER_UNAVAILABLE -> {
                onServerUnavailable()
            }
            Constants.KEY_UNSUCCESSFUL_REQUEST, INTERNAL_SERVER_ERROR -> {
                when (errorDTO.message) {
                    Constants.KEY_USER_ALREADY_EXISTS -> {
                        onUserExists()
                    }
                    else -> {
                        onUnsuccessfulRequest(getInternalizedMessage(Constants.KEY_INTERNAL_SERVER_ERROR))
                    }
                }

            }
            else -> {
                onUnsuccessfulRequest(getInternalizedMessage(Constants.KEY_INTERNAL_SERVER_ERROR))
            }
        }
    }

    private fun onUserExists() {
        view!!.showSnackBar(viewComponent!!, R.string.userAlreadyExists)
    }

    override fun onUnsuccessfulRequest(message: String?) {
        if (!StringUtils.isEmpty(message)) {
            view!!.showSnackBar(viewComponent!!, getInternalizedMessage(Constants.KEY_INTERNAL_SERVER_ERROR))
        }
    }

    override fun onServerUnavailable() {
        when {
            connectivityStatus == TYPE_NOT_CONNECTED -> view!!.showAlertDialog(R.string.noInternetConnection, R.string.noInternetConnectionMessage, R.string.retry, false)
            !viewIsReady!! -> view!!.showAlertDialog(R.string.serverIsUnavailable, R.string.serverIsUnavailableMessage, R.string.retry, false)
            else -> view!!.showSnackBar(viewComponent!!, getInternalizedMessage(KEY_SERVER_UNAVAILABLE))
        }
    }

    override fun loadSpinnersData() {
        view!!.showProgress()
        loadCoaches()
    }

    override fun registration(userDTO: ExtendedUserDTO, placeImageUri: String?) {
        view!!.disableButton()
        try {
            fillUserBySpinnerValues(userDTO)
            Validator.validateUserData(userDTO, true)
            setUserData(userDTO)
            userDTO.password = Util.getEncodedPassword(userDTO.password!!)
            view!!.showProgress()
            userConnection.registration(UserDTO(userDTO), compressImage(placeImageUri))
        } catch (e: Exception) {
            view!!.enableButton()
            view!!.hideProgress()
            view!!.showSnackBar(viewComponent!!, e.localizedMessage)
        }
    }

    private fun fillUserBySpinnerValues(userDTO: ExtendedUserDTO) {
        userDTO.selectedCoachIndex = selectedCoachIndex
        userDTO.selectedCountryIndex = selectedCountryIndex
        userDTO.selectedRankIndex = selectedRankIndex
        userDTO.selectedGenderIndex = selectedGenderIndex
    }

    private fun setUserData(@NonNull userDTO: UserDTO) {
        if (selectedRankIndex != NOT_SELECTED_INDEX && selectedRankIndex != ABSENCE_INDEX) {
            userDTO.rank = ranksIndexes[selectedRankIndex.minus(2)]
        }
        if (selectedCountryIndex != NOT_SELECTED_INDEX) {
            userDTO.country = countriesIndexes[selectedCountryIndex.minus(1)]
        }
        if (selectedCoachIndex != NOT_SELECTED_INDEX) {
            userDTO.coach = coachesIndexes[selectedCoachIndex.minus(2)]
        }
        if (selectedGenderIndex != NOT_SELECTED_INDEX) {
            userDTO.beMale = isMale(selectedGenderIndex - 1)
        }
        userDTO.beCoach = false
        userDTO.beOrganizer = false
        userDTO.beAdmin = false
    }

    override fun setConnectivityStatus(status: Int?) {
        this.connectivityStatus = status
        if (firstTimeSpinnerLoad) {
            firstTimeSpinnerLoad = false
            if (!Util.isConnected(status)) {
                view!!.showAlertDialog(R.string.noInternetConnection, R.string.noInternetConnectionMessage, R.string.retry, false)
            } else if ((!viewIsReady!!)) {
                loadSpinnersData()
            }
        }
    }

    override fun loadCoaches() {
        userConnection.getCoaches(packageModel!!.getValue(Constants.TOKEN))
    }

    override fun loadRanks() {
        userConnection.getRanks()
    }

    override fun loadCountries() {
        userConnection.getCountries()
    }

    override fun spinnersAreLoaded(): Boolean {
        return !CollectionUtils.isEmpty(countriesIndexes) && !CollectionUtils.isEmpty(coachesIndexes) && !CollectionUtils.isEmpty(ranksIndexes)
    }

    override fun attachView(activity: RegistrationContractView) {
        view = activity
    }

    override fun attachViewComponent(view: View) {
        this.viewComponent = view
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

    private fun saveCoachesIndexes(coaches: List<String>) {
        for (i in coaches.indices) {
            coachesIndexes[i] = coaches[i]
        }
    }

    private fun isMale(index: Int?): Boolean {
        return index == 0
    }
}
