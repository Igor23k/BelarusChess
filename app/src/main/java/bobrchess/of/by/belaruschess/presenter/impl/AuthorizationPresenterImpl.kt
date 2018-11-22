package bobrchess.of.by.belaruschess.presenter.impl

import android.view.View
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.exception.IncorrectDataException
import bobrchess.of.by.belaruschess.network.connection.AuthorizationConnection
import bobrchess.of.by.belaruschess.presenter.AuthorizationPresenter
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAuthorization
import bobrchess.of.by.belaruschess.util.Constants
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.util.Validator
import bobrchess.of.by.belaruschess.view.activity.AuthorizationContractView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

@InjectViewState
class AuthorizationPresenterImpl : MvpPresenter<AuthorizationContractView>, CallBackAuthorization, AuthorizationPresenter {

    private var view: AuthorizationContractView? = null
    private var viewComponent: View? = null
    private var userConnection: AuthorizationConnection = AuthorizationConnection()
    private var viewIsReady: Boolean? = false
    private var connectivityStatus: Int? = null

    constructor(){
        userConnection.attachPresenter(this)
    }

    override fun setConnectivityStatus(connectivityStatus: Int?) {
        this.connectivityStatus = connectivityStatus
        if (connectivityStatus == Util.TYPE_NOT_CONNECTED) {
            view!!.showNoConnectionAlertDialog(R.string.noInternetConnection, R.string.noInternetConnectionMessage, R.string.retry, false)
        }
    }

    override fun attachViewComponent(viewComponent: View) {
        this.viewComponent = viewComponent
    }

    override fun onResponse(userDTO: UserDTO) {
        view!!.hideProgress()
        view!!.enableButton()
        view!!.startActivity(userDTO)
    }

    override fun onFailure(t: Throwable) {
        view!!.hideProgress()
        view!!.enableButton()
        when (t.message) {
            Constants.SERVER_UNAVAILABLE -> {
                onServerUnavailable()
            }
            Constants.UNSUCCESSFUL_REQUEST -> {
                onUnsuccessfulRequest()
            }
        }
    }

    private fun onUnsuccessfulRequest() {
        view!!.showSnackBar(viewComponent, R.string.internalServerError, R.string.retry)
    }

    private fun onServerUnavailable() {
        when (connectivityStatus) {
            Util.TYPE_NOT_CONNECTED -> view!!.showNoConnectionAlertDialog(R.string.noInternetConnection, R.string.noInternetConnectionMessage, R.string.retry, false)
            else -> view!!.showSnackBar(viewComponent, R.string.serverIsUnavailable, R.string.retry)
        }
    }

    override fun authorization(userDTO: UserDTO) {
        view!!.disableButton()
        try {
            Validator.validateAuthUserData(userDTO)
            userDTO.password = Util.getEncodedPassword(userDTO.password!!)
            view!!.showProgress()
            userConnection.authorization(userDTO)
        } catch (e: IncorrectDataException) {
            view!!.enableButton()
            view!!.showSnackBar(viewComponent, e.localizedMessage, R.string.retry)// bug нужна видимо интернационализация, в регистрации то же самое
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
