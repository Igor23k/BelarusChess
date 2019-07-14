package bobrchess.of.by.belaruschess.presenter.impl

import android.view.View
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.ErrorDTO
import bobrchess.of.by.belaruschess.dto.TokenDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.network.connection.AuthorizationConnection
import bobrchess.of.by.belaruschess.network.connection.TokenConnection
import bobrchess.of.by.belaruschess.presenter.TokenAuthPresenter
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAuthorization
import bobrchess.of.by.belaruschess.presenter.callback.CallBackToken
import bobrchess.of.by.belaruschess.util.Constants.Companion.REFRESH_TOKEN
import bobrchess.of.by.belaruschess.util.Constants.Companion.REFRESH_TOKEN_DEFAULT
import bobrchess.of.by.belaruschess.util.Constants.Companion.TOKEN
import bobrchess.of.by.belaruschess.util.Constants.Companion.TOKEN_DEFAULT
import bobrchess.of.by.belaruschess.util.Constants.Companion.TOKEN_IS_EXPIRED
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.view.activity.AuthorizationContractView
import bobrchess.of.by.belaruschess.view.activity.impl.PackageModel
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.gson.Gson
import org.springframework.util.StringUtils

@InjectViewState
class TokenAuthPresenterImpl : MvpPresenter<AuthorizationContractView>, CallBackAuthorization, CallBackToken, TokenAuthPresenter {

    private var view: AuthorizationContractView? = null
    private var viewComponent: View? = null
    private var authorizationConnection: AuthorizationConnection = AuthorizationConnection()
    private var tokenConnection: TokenConnection = TokenConnection()
    private var viewIsReady: Boolean? = false
    private var connectivityStatus: Int? = null
    private var packageModel: PackageModel? = null

    constructor() {
        authorizationConnection.attachPresenter(this)
        tokenConnection.attachPresenter(this)
    }

    override fun setConnectivityStatus(status: Int?) {
        this.connectivityStatus = status
        if (connectivityStatus == Util.TYPE_NOT_CONNECTED) {
            view!!.showNoConnectionAlertDialog(R.string.noInternetConnection, R.string.noInternetConnectionMessage, R.string.retry, false)
        }
    }

    override fun attachViewComponent(view: View) {
        this.viewComponent = view
    }

    override fun onResponse(userDTO: UserDTO) {
        view!!.startActivity(userDTO)
    }

    override fun onResponse(tokenDTO: TokenDTO) {
        packageModel!!.putValue(TOKEN, tokenDTO.token)
        tokenAuthorization()
    }

    override fun onFailure(t: Throwable) {
        val errorDto = Gson().fromJson(t.message, ErrorDTO :: class.java)
        if (TOKEN_IS_EXPIRED == errorDto.message) {
            refreshToken()
        }else {
            view!!.unsuccessfulTokenAuth()
        }
    }

    fun setPackageModel(packageModel: PackageModel) {
        this.packageModel = packageModel
    }

    override fun isAuthenticated(): Boolean {
        return true
        // return !StringUtils.isEmpty(packageModel!!.getValue(TOKEN))
    }

    override fun tokenAuthorization() {
        // val token = packageModel!!.getValue(TOKEN)
        authorizationConnection.getUser(TOKEN_DEFAULT)
    }

    fun refreshToken() {
        val refreshToken = /*packageModel!!.getValue(REFRESH_TOKEN)*/REFRESH_TOKEN_DEFAULT
        if (!StringUtils.isEmpty(refreshToken)) {
            tokenConnection.refreshToken(refreshToken)
        } else {
            //enter login and pass
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
}
