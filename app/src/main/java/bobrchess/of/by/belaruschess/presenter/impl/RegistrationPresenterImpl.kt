package bobrchess.of.by.belaruschess.presenter.impl

import android.support.annotation.NonNull
import android.view.View
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.*
import bobrchess.of.by.belaruschess.exception.IncorrectDataException
import bobrchess.of.by.belaruschess.network.connection.RegistrationConnection
import bobrchess.of.by.belaruschess.presenter.RegistrationPresenter
import bobrchess.of.by.belaruschess.presenter.callback.CallBackRegistration
import bobrchess.of.by.belaruschess.util.Constants
import bobrchess.of.by.belaruschess.util.Constants.Companion.INTERNAL_SERVER_ERROR
import bobrchess.of.by.belaruschess.util.Constants.Companion.KEY_SERVER_UNAVAILABLE
import bobrchess.of.by.belaruschess.util.Constants.Companion.SERVER_UNAVAILABLE
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.util.Util.Companion.TYPE_NOT_CONNECTED
import bobrchess.of.by.belaruschess.util.Util.Companion.getInternalizedMessage
import bobrchess.of.by.belaruschess.util.Validator
import bobrchess.of.by.belaruschess.view.activity.PackageModel
import bobrchess.of.by.belaruschess.view.activity.RegistrationContractView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.springframework.util.StringUtils
import java.util.*


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
    private val coachesIndexes = HashMap<Int, UserDTO>()
    private var connectivityStatus: Int? = null
    private var firstTimeSpinnerLoad: Boolean = true
    private var packageModel: PackageModel? = null

    companion object {
        private const val NOT_SELECTED_INDEX = 0
        private const val ABSENCE_INDEX = 1
    }

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
        saveCoachesIndexes(coaches)
        view!!.setCoachSpinnerAdapter(Util.getUsersBasicData(coaches).toMutableList())
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
                onUnsuccessfulRequest(errorDTO.message)
            }
        }
    }

    override fun onUnsuccessfulRequest(message: String?) {
        if (!StringUtils.isEmpty(message)) {
            view!!.showSnackBar(viewComponent!!, message.toString())//todo переделать чтобы тут была своя строка ибо когда переключим на русский то тут все равно будет англ. ПОпропробовать заюзать интернационализацию
        }
    }

    override fun onServerUnavailable() {
         when {
             connectivityStatus == TYPE_NOT_CONNECTED -> view!!.showAlertDialog(R.string.noInternetConnection, R.string.noInternetConnectionMessage, R.string.retry, false)
             !viewIsReady!! -> view!!.showAlertDialog(getInternalizedMessage(KEY_SERVER_UNAVAILABLE), R.string.serverIsUnavailableMessage, R.string.retry, false)
             else -> view!!.showSnackBar(viewComponent!!, getInternalizedMessage(KEY_SERVER_UNAVAILABLE))
         }
    }

    override fun loadSpinnersData() {
        view!!.showProgress()
        loadCoaches()
    }

    override fun registration(userDTO: RegistrationUserDTO) {
        view!!.disableButton()
        try {
            fillUserBySpinnerValues(userDTO)
            Validator.validateUserData(userDTO)
            setUserData(userDTO)
            userDTO.password = Util.getEncodedPassword(userDTO.password!!)
            view!!.showProgress()
            userConnection.registration(UserDTO(userDTO))
        } catch (e: IncorrectDataException) {
            view!!.enableButton()
            view!!.showSnackBar(viewComponent!!, e.localizedMessage)
        }
    }

    private fun fillUserBySpinnerValues(userDTO: RegistrationUserDTO) {
        userDTO.selectedCoachIndex = selectedCoachIndex
        userDTO.selectedCountryIndex = selectedCountryIndex
        userDTO.selectedRankIndex = selectedRankIndex
        userDTO.selectedGenderIndex = selectedGenderIndex
    }

    private fun setUserData(@NonNull userDTO: UserDTO) {
        if (selectedRankIndex != NOT_SELECTED_INDEX && selectedGenderIndex != ABSENCE_INDEX) {
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
        userDTO.beCoach = true// todo bug думать, думать
        userDTO.beOrganizer = true
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

    private fun saveCoachesIndexes(coaches: List<UserDTO>) {
        for (i in coaches.indices) {
            coachesIndexes[i] = coaches[i]
        }
    }

    private fun isMale(index: Int?): Boolean {
        return index == 0
    }
}
