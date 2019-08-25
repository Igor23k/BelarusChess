package bobrchess.of.by.belaruschess.presenter.impl

import android.view.View
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.ErrorDTO
import bobrchess.of.by.belaruschess.dto.UserContextDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.exception.IncorrectDataException
import bobrchess.of.by.belaruschess.network.connection.AuthorizationConnection
import bobrchess.of.by.belaruschess.presenter.AuthorizationPresenter
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAuthorization
import bobrchess.of.by.belaruschess.util.Constants
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.util.Validator
import bobrchess.of.by.belaruschess.view.activity.AuthorizationContractView
import bobrchess.of.by.belaruschess.view.activity.PackageModel
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.springframework.util.StringUtils

@InjectViewState
class AuthorizationPresenterImpl : MvpPresenter<AuthorizationContractView>(), CallBackAuthorization, AuthorizationPresenter {

    private var view: AuthorizationContractView? = null
    private var viewComponent: View? = null
    private var authorizationConnection: AuthorizationConnection = AuthorizationConnection()
    private var viewIsReady: Boolean? = false
    private var connectivityStatus: Int? = null
    private var packageModel: PackageModel? = null

    init {
        authorizationConnection.attachPresenter(this)
    }

    override fun setConnectivityStatus(status: Int?) {
        this.connectivityStatus = status
        if (!Util.isConnected(status)) {
            view?.showAlertDialog(R.string.noInternetConnection, R.string.noInternetConnectionMessage, R.string.retry, false)
        } else {
            view?.dismissAlertDialog()
            view?.tokenAuthorization()
        }
    }

    override fun attachViewComponent(view: View) {
        this.viewComponent = view
    }

    override fun onResponse(userContextDTO: UserContextDTO) {
        packageModel?.putTokenMap(userContextDTO.tokenMap)
        view!!.hideProgress()
        view!!.enableButton()
        view!!.startActivity(userContextDTO.user)
    }

    override fun onResponse(userDTO: UserDTO) {
        view!!.hideProgress()
        view!!.enableButton()
        view!!.startActivity(userDTO)
    }

    override fun onFailure(errorDTO: ErrorDTO) {
        view!!.hideProgress()
        view!!.enableButton()

        when (errorDTO.error) {
            Constants.SERVER_UNAVAILABLE -> {
                onServerUnavailable()
            }
            Constants.KEY_UNSUCCESSFUL_REQUEST, Constants.INTERNAL_SERVER_ERROR -> {
                onUnsuccessfulRequest(errorDTO.message)
            }
            else -> {
                when (errorDTO.message) {
                    Constants.KEY_INVALID_EMAIL_OR_PASSWORD -> {
                        onInvalidEmailOrPassword()
                    }
                    else -> {
                        onUnsuccessfulRequest(errorDTO.message)
                    }
                }
            }

        }
    }

    private fun onInvalidEmailOrPassword() {
        view!!.showSnackBar(viewComponent!!, R.string.incorrectEmailOrPassword)
    }

    override fun onUnsuccessfulRequest(message: String?) {
        if (!StringUtils.isEmpty(message)) {
            view!!.showSnackBar(viewComponent!!, message.toString())//todo переделать чтобы тут была своя строка ибо когда переключим на русский то тут все равно будет англ. ПОпропробовать заюзать интернационализацию
        }
    }

    override fun onServerUnavailable() {
        when (connectivityStatus) {
            Util.TYPE_NOT_CONNECTED -> view!!.showAlertDialog(R.string.noInternetConnection, R.string.noInternetConnectionMessage, R.string.retry, false)
            else -> view!!.showSnackBar(viewComponent!!, R.string.serverIsUnavailable, R.string.retry)
        }
    }

    public fun setPackageModel(packageModel: PackageModel) {
        this.packageModel = packageModel
    }

    override fun authorization(userDTO: UserDTO) {
        view!!.disableButton()
        try {
            Validator.validateAuthUserData(userDTO)
            userDTO.password = Util.getEncodedPassword(userDTO.password!!)
            view!!.showProgress()
            authorizationConnection.authorization(userDTO)
        } catch (e: IncorrectDataException) {
            view!!.enableButton()
            view!!.showSnackBar(viewComponent!!, e.localizedMessage, R.string.retry)// todo bug нужна видимо интернационализация, в регистрации то же самое
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
