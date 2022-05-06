package bobrchess.of.by.belaruschess.network.connection.internal

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.presenter.callback.CallBackPasswordReset
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.util.Util.Companion.buildErrorDto
import org.apache.commons.httpclient.HttpStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordResetConnection {

    private var callBack: CallBackPasswordReset? = null

    fun passwordReset(passwordResetHeader: String, email: String) {
        App.getPersonalServerApi().passwordReset(passwordResetHeader, email).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack!!.onResponse(response.body())
                    } else {
                        callBack!!.onFailure(buildErrorDto(response.errorBody().string()))
                    }
                } else {
                    callBack!!.onFailure(buildErrorDto(response.errorBody().string()))
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun attachPresenter(callBack: CallBackPasswordReset) {
        this.callBack = callBack
    }
}
