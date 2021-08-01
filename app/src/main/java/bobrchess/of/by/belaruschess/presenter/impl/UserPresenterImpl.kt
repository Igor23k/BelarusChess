package bobrchess.of.by.belaruschess.presenter.impl

import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.CountryDTO
import bobrchess.of.by.belaruschess.dto.ErrorDTO
import bobrchess.of.by.belaruschess.dto.RankDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.dto.extended.ExtendedUserDTO
import bobrchess.of.by.belaruschess.exception.IncorrectDataException
import bobrchess.of.by.belaruschess.network.connection.internal.SearchUserConnection
import bobrchess.of.by.belaruschess.presenter.UserPresenter
import bobrchess.of.by.belaruschess.presenter.callback.CallBackSearchUser
import bobrchess.of.by.belaruschess.util.Constants
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.util.Validator.validateUserData
import bobrchess.of.by.belaruschess.view.activity.PackageModel
import bobrchess.of.by.belaruschess.view.activity.UserContractView
import butterknife.BindView
import org.springframework.util.StringUtils
import java.util.*

class UserPresenterImpl : CallBackSearchUser, UserPresenter {
    private var view: UserContractView? = null
    private val userConnection: SearchUserConnection
    private var viewIsReady = false
    private var packageModel: PackageModel? = null
    private var connectivityStatus: Int? = 0
    private var selectedRankIndex = 0
    private var selectedCountryIndex = 0
    private var selectedCoachIndex = 0
    private var selectedGenderIndex = 0
    private val ranksIndexes: MutableMap<Int, RankDTO?> = HashMap()
    private val countriesIndexes: MutableMap<Int, CountryDTO?> = HashMap()
    private val coachesIndexes: MutableMap<Int, String> = HashMap()

    @JvmField
    @BindView(R.id.t_link_registration)
    var registrationLink: TextView? = null

    override fun loadUserById(id: Int) {
        view!!.showProgress()
        userConnection.getUserById(packageModel!!.getValue(Constants.TOKEN), id)
    }

    override fun loadReferees() {
        view!!.showProgress()
        userConnection.getReferees(packageModel!!.getValue(Constants.TOKEN))
    }

    override fun loadCoaches() {
        view!!.showProgress()
        userConnection.getCoaches(packageModel!!.getValue(Constants.TOKEN))
    }

    override fun loadUsers() {
        view!!.showProgress()
        userConnection.getUsers(10, packageModel!!.getValue(Constants.TOKEN))
    } //todo

    override fun loadUsers(count: Int?) {
        view!!.showProgress()
        userConnection.getUsers(count, packageModel!!.getValue(Constants.TOKEN))
    }

    override fun searchUsers(text: String) {
        if (viewIsReady) {
            view!!.showProgress()
            userConnection.searchUsers(text, packageModel!!.getValue(Constants.TOKEN))
        }
    }

    override fun updateUser(user: ExtendedUserDTO) {
        if (viewIsReady) {
            //view.disableButton();
            try {
                user.selectedCoachIndex = selectedCoachIndex - 1
                user.selectedCountryIndex = selectedCountryIndex
                user.selectedRankIndex = selectedRankIndex
                user.selectedGenderIndex = selectedGenderIndex
                validateUserData(user, false)
                user.coach = coachesIndexes[selectedCoachIndex - 1]
                user.country = countriesIndexes[selectedCountryIndex]
                user.rank = ranksIndexes[selectedRankIndex]
                user.beMale = selectedGenderIndex == 0
                //user.setCoach(null);
                //   view.disableButton();
                view!!.showProgress()
                userConnection.updateUser(UserDTO(user), packageModel!!.getValue(Constants.TOKEN))
            } catch (e: IncorrectDataException) {
                view!!.showToast(e.localizedMessage) //todo проверить что норм показывается и остальные так же плэйс и тд
                view!!.hideProgress()
                //    view.enableButton();
            }
        }
    }

    override fun setPackageModel(packageModel: PackageModel?){
        this.packageModel = packageModel
    }

    override fun attachView(userContractView: UserContractView) {
        view = userContractView
    }

    override fun detachView() {
        view = null
    }

    override fun viewIsReady() {
        viewIsReady = true
    }

    override fun onResponse(users: List<UserDTO>) {
        view!!.showUsers(users)
        view!!.hideProgress()
    }

    override fun onResponse(user: UserDTO) {
        view!!.showUser(user)
        view!!.hideProgress()
    }

    override fun onFailure(errorDTO: ErrorDTO) {
        view!!.hideProgress()
        when (errorDTO.error) {
            Constants.SERVER_UNAVAILABLE -> {
                onServerUnavailable()
            }
             Constants.KEY_UNSUCCESSFUL_REQUEST, Constants.INTERNAL_SERVER_ERROR -> {
                when (errorDTO.message) {
                    Constants.KEY_USER_ALREADY_EXISTS -> {
                        onUserExists()
                    }
                    else -> {
                        onUnsuccessfulRequest(Util.getInternalizedMessage(Constants.KEY_INTERNAL_SERVER_ERROR))
                    }
                }

            }
            else -> {
                onUnsuccessfulRequest(Util.getInternalizedMessage(Constants.KEY_INTERNAL_SERVER_ERROR))
            }
        }
    }

    private fun onUserExists() {
        view!!.showSnackbar(R.string.userAlreadyExists)
    }

    override fun onServerUnavailable() {
        view!!.hideProgress()
    }

    override fun onUnsuccessfulRequest(message: String?) {
        view!!.hideProgress()
    }

    override fun setConnectivityStatus(status: Int?) {
        connectivityStatus = status
    }

    override fun isConnected(status: Int): Boolean {
        return Util.isConnected(status)
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

    override fun saveRanksIndexes(ranks: List<RankDTO?>?) {
        if (ranks != null) {
            for (i in ranks.indices) {
                ranksIndexes[i] = ranks[i]
            }
        }
    }

    override fun saveCoachesIndexes(coaches: List<UserDTO?>?) {
        if (coaches != null) {
            for (i in coaches.indices) {
                var rank = coaches[i]!!.rank?.abbreviation
                rank = if (!StringUtils.isEmpty(rank)) " ($rank)" else ""
                coachesIndexes[i] = coaches[i]!!.name + " " + coaches[i]!!.surname + rank
            }
        }
    }

    override fun saveCountriesIndexes(countries: List<CountryDTO?>?) {
        if (countries != null) {
            for (i in countries.indices) {
                countriesIndexes[i] = countries[i]
            }
        }
    }

    init {
        userConnection = SearchUserConnection()
        userConnection.attachPresenter(this)
    }
}