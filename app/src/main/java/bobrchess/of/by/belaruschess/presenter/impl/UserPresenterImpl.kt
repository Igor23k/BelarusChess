package bobrchess.of.by.belaruschess.presenter.impl

import android.widget.TextView
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.*
import bobrchess.of.by.belaruschess.dto.extended.ExtendedUserDTO
import bobrchess.of.by.belaruschess.exception.IncorrectDataException
import bobrchess.of.by.belaruschess.network.connection.internal.SearchUserConnection
import bobrchess.of.by.belaruschess.network.connection.internal.TokenConnection
import bobrchess.of.by.belaruschess.presenter.UserPresenter
import bobrchess.of.by.belaruschess.presenter.callback.CallBackSearchUser
import bobrchess.of.by.belaruschess.presenter.callback.CallBackToken
import bobrchess.of.by.belaruschess.util.Constants
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.util.Validator.validateUserData
import bobrchess.of.by.belaruschess.view.activity.PackageModel
import bobrchess.of.by.belaruschess.view.activity.UserContractView
import butterknife.BindView
import org.springframework.util.StringUtils
import java.io.File

class UserPresenterImpl : CallBackSearchUser, CallBackToken, UserPresenter {
    private var view: UserContractView? = null
    private var userConnection: SearchUserConnection = SearchUserConnection()
    private var tokenConnection: TokenConnection = TokenConnection()
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
    var updatedUser: UserDTO? = null
    var updatedUserImage: File? = null

    @JvmField
    @BindView(R.id.t_link_registration)
    var registrationLink: TextView? = null

    override fun loadUserById(id: Int) {
        view!!.showProgress()
        userConnection.getUserById(packageModel!!.getSharePrefValue(Constants.TOKEN), id)
    }

    override fun loadReferees() {
        //view!!.showProgress()
        userConnection.getReferees(packageModel!!.getSharePrefValue(Constants.TOKEN))
    }

    override fun loadCoaches() {
      //  view!!.showProgress()
        userConnection.getCoaches()
    }

    override fun loadUsers(count: Int?) {
        view!!.showProgress()
        userConnection.getUsers(count, packageModel!!.getSharePrefValue(Constants.TOKEN))
    }

    override fun searchUsers(text: String) {
        if (viewIsReady) {
            view!!.showProgress()
            userConnection.searchUsers(text, packageModel!!.getSharePrefValue(Constants.TOKEN))
        }
    }

    override fun updateUser(user: ExtendedUserDTO, userImage: File?) {
        if (viewIsReady) {
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
                view!!.showProgress()

                updatedUser = user
                updatedUserImage = Util.compressImage(userImage)

                userConnection.updateUser(updatedUser!!, updatedUserImage, packageModel!!.getSharePrefValue(Constants.TOKEN))
            } catch (e: IncorrectDataException) {
                view!!.showToast(e.localizedMessage)
                view!!.hideProgress()
            }
        }
    }

    override fun setPackageModel(packageModel: PackageModel?) {
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

    override fun onResponse(tokenDTO: TokenDTO) {
        packageModel!!.addSharePref(Constants.TOKEN, tokenDTO.token)

        if (updatedUser != null) {
            userConnection.updateUser(updatedUser!!, updatedUserImage, packageModel!!.getSharePrefValue(Constants.TOKEN))
        } else {
            loadReferees()
            loadCoaches()
        }
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
                when (errorDTO.message) {
                    Constants.TOKEN_IS_EXPIRED_MESSAGE -> {
                        view!!.showProgress()
                        refreshToken()
                    }
                    else -> {
                        onUnsuccessfulRequest(Util.getInternalizedMessage(Constants.KEY_INTERNAL_SERVER_ERROR))
                    }
                }
            }
        }
    }

    private fun refreshToken() {
        val refreshToken = packageModel!!.getSharePrefValue(Constants.REFRESH_TOKEN)
        if (!StringUtils.isEmpty(refreshToken)) {
            tokenConnection.refreshToken(refreshToken)
        } else {
            onUnsuccessfulRequest(Util.getInternalizedMessage(Constants.KEY_INTERNAL_SERVER_ERROR))
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
        userConnection.attachPresenter(this)
        tokenConnection.attachPresenter(this)
    }
}