package bobrchess.of.by.belaruschess.presenter.impl

import android.view.View
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.ErrorDTO
import bobrchess.of.by.belaruschess.dto.TokenDTO
import bobrchess.of.by.belaruschess.dto.UserContextDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.network.connection.internal.AuthorizationConnection
import bobrchess.of.by.belaruschess.network.connection.internal.TokenConnection
import bobrchess.of.by.belaruschess.presenter.TokenAuthPresenter
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAuthorization
import bobrchess.of.by.belaruschess.presenter.callback.CallBackToken
import bobrchess.of.by.belaruschess.util.Constants
import bobrchess.of.by.belaruschess.util.Constants.Companion.REFRESH_TOKEN
import bobrchess.of.by.belaruschess.util.Constants.Companion.TOKEN
import bobrchess.of.by.belaruschess.util.Constants.Companion.TOKEN_IS_EXPIRED_MESSAGE
import bobrchess.of.by.belaruschess.util.Constants.Companion.UNAUTHORIZED
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.view.activity.AuthorizationContractView
import bobrchess.of.by.belaruschess.view.activity.PackageModel
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.springframework.util.StringUtils

@InjectViewState
class TokenAuthPresenterImpl : MvpPresenter<AuthorizationContractView>(), CallBackAuthorization, CallBackToken, TokenAuthPresenter {

    private var view: AuthorizationContractView? = null
    private var viewComponent: View? = null
    private var authorizationConnection: AuthorizationConnection = AuthorizationConnection()
    private var tokenConnection: TokenConnection = TokenConnection()
    private var viewIsReady: Boolean? = false
    private var connectivityStatus: Int? = 0
    private var packageModel: PackageModel? = null

    init {
        authorizationConnection.attachPresenter(this)
        tokenConnection.attachPresenter(this)
    }

    override fun setConnectivityStatus(status: Int?) {
        this.connectivityStatus = status
    }

    override fun attachViewComponent(view: View) {
        this.viewComponent = view
    }

    override fun onResponse(userContextDTO: UserContextDTO) {
        view!!.startActivity(userContextDTO.user)
    }

    override fun onResponse(userDTO: UserDTO) {
        view!!.startActivity(userDTO)
    }

    override fun onResponse(tokenDTO: TokenDTO) {
        packageModel!!.putValue(TOKEN, tokenDTO.token)
        tokenAuthorization()
    }

    override fun onServerUnavailable() {
        when {
            connectivityStatus == Util.TYPE_NOT_CONNECTED -> view!!.showAlertDialog(R.string.noInternetConnection, R.string.noInternetConnectionMessage, R.string.retry, false)
            !viewIsReady!! -> view!!.showAlertDialog(R.string.serverIsUnavailable, R.string.serverIsUnavailableMessage, R.string.retry, false)
            else -> view!!.showSnackBar(viewComponent!!, R.string.serverIsUnavailable, R.string.retry)
        }
    }

    override fun onFailure(errorDTO: ErrorDTO) {
        when (errorDTO.error) {
            Constants.SERVER_UNAVAILABLE -> {
                onServerUnavailable()
            }
            UNAUTHORIZED -> {
                when (errorDTO.message) {
                    TOKEN_IS_EXPIRED_MESSAGE -> {
                        refreshToken()
                    }
                    else -> {
                        view!!.unsuccessfulTokenAuth()
                    }
                }
            }
            else -> {
                onServerUnavailable()
            }

        }
    }

    fun setPackageModel(packageModel: PackageModel) {
        this.packageModel = packageModel
    }

    override fun isAuthenticated(): Boolean {
        return !StringUtils.isEmpty(packageModel!!.getValue(TOKEN))
    }

    override fun tokenAuthorization() {
        val token = packageModel!!.getValue(TOKEN)
        authorizationConnection.getUser(token)
    }

    private fun refreshToken() {
        val refreshToken = packageModel!!.getValue(REFRESH_TOKEN)
        if (!StringUtils.isEmpty(refreshToken)) {
            tokenConnection.refreshToken(refreshToken)
        } else {
            //enter login and pass todo
        }
    }

    override fun attachView(activity: AuthorizationContractView) {
        view = activity
    }

    override fun detachView() {
        view = null
    }

    override fun viewIsReady() {
        viewIsReady = true
    }

    override fun onUnsuccessfulRequest(message: String?) {

    }
}
