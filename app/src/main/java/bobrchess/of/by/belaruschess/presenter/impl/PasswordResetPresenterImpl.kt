package bobrchess.of.by.belaruschess.presenter.impl

import android.view.View
import bobrchess.of.by.belaruschess.R
import bobrchess.of.by.belaruschess.dto.ErrorDTO
import bobrchess.of.by.belaruschess.exception.IncorrectDataException
import bobrchess.of.by.belaruschess.network.connection.internal.PasswordResetConnection
import bobrchess.of.by.belaruschess.presenter.PasswordResetPresenter
import bobrchess.of.by.belaruschess.presenter.callback.CallBackPasswordReset
import bobrchess.of.by.belaruschess.util.Constants
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.util.Validator
import bobrchess.of.by.belaruschess.view.activity.PackageModel
import bobrchess.of.by.belaruschess.view.activity.PasswordResetContractView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.springframework.util.StringUtils

@InjectViewState
class PasswordResetPresenterImpl : MvpPresenter<PasswordResetContractView>(), CallBackPasswordReset, PasswordResetPresenter {

    private var view: PasswordResetContractView? = null
    private var viewComponent: View? = null
    private var passwordResetConnection: PasswordResetConnection = PasswordResetConnection()
    private var viewIsReady: Boolean? = false
    private var connectivityStatus: Int? = null
    private var packageModel: PackageModel? = null

    init {
        passwordResetConnection.attachPresenter(this)
    }

    override fun setConnectivityStatus(status: Int?) {
        this.connectivityStatus = status
        if (!Util.isConnected(status)) {
            view?.dismissAlertDialog()
            view?.showAlertDialog(R.string.noInternetConnection, R.string.noInternetConnectionMessage, R.string.retry, false)
        } else {//todo
            //view?.dismissAlertDialog()
            //view?.tokenAuthorization()
        }
    }

    override fun attachViewComponent(view: View) {
        this.viewComponent = view
    }

    override fun onResponse(result: Boolean) {
        if (result) {
            view!!.hideProgress()
            view!!.enableButton()
            //view!!.startActivity(userContextDTO.user)
        } else {
            view!!.hideProgress()
            view!!.enableButton()
            onServerUnavailable()
        }
    }

    override fun onFailure(errorDTO: ErrorDTO) {
        view!!.hideProgress()
        view!!.enableButton()

        if (errorDTO.status == 401) {
            onInvalidEmailOrPassword()
        } else {

            when (errorDTO.error) {
                Constants.SERVER_UNAVAILABLE -> {
                    onServerUnavailable()
                }
                Constants.KEY_UNSUCCESSFUL_REQUEST, Constants.INTERNAL_SERVER_ERROR -> {
                    onUnsuccessfulRequest(Util.getInternalizedMessage(Constants.KEY_INTERNAL_SERVER_ERROR))
                }
                else -> {
                    when (errorDTO.message) {
                        Constants.KEY_INVALID_EMAIL_OR_PASSWORD -> {
                            onInvalidEmailOrPassword()
                        }
                        else -> {
                            onUnsuccessfulRequest(Util.getInternalizedMessage(Constants.KEY_INTERNAL_SERVER_ERROR))
                        }
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
            view!!.showSnackBar(viewComponent!!, Util.getInternalizedMessage(Constants.KEY_INTERNAL_SERVER_ERROR))
        }
    }

    override fun onServerUnavailable() {
        when (connectivityStatus) {
            Util.TYPE_NOT_CONNECTED -> view!!.showAlertDialog(R.string.noInternetConnection, R.string.noInternetConnectionMessage, R.string.retry, false)
            else -> view!!.showSnackBar(viewComponent!!, R.string.serverIsUnavailable, R.string.retry)
        }
    }

    fun setPackageModel(packageModel: PackageModel) {
        this.packageModel = packageModel
    }

    override fun reset(userEmail: String) {
        view!!.disableButton()
        try {
            Validator.validateEmail(userEmail)
            view!!.showProgress()
            passwordResetConnection.passwordReset(packageModel!!.getValue(Constants.TOKEN), userEmail)
        } catch (e: IncorrectDataException) {
            view!!.enableButton()
            view!!.showSnackBar(viewComponent!!, e.localizedMessage)
        }
    }

    override fun attachView(activity: PasswordResetContractView) {
        view = activity
    }

    override fun detachView() {
        view = null
    }

    override fun viewIsReady() {
        viewIsReady = true
    }
}