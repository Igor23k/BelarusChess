package bobrchess.of.by.belaruschess.network.connection

import org.apache.commons.httpclient.HttpStatus

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAuthorization
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import bobrchess.of.by.belaruschess.util.Constants.Companion.ERROR_PARAMETER
import bobrchess.of.by.belaruschess.util.Constants.Companion.SERVER_UNAVAILABLE
import bobrchess.of.by.belaruschess.util.Constants.Companion.UNSUCCESSFUL_REQUEST

/**
 * Created by Igor on 11.04.2018.
 */

class AuthorizationConnection {

    private var callBack: CallBackAuthorization? = null

    fun authorization(userDTO: UserDTO) {
        App.getAPI().authorization(userDTO).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if (response.isSuccessful) {
                    if (response.raw().code() == HttpStatus.SC_OK && response.body() != null) {
                        callBack!!.onResponse(response.body())
                    } else {
                        callBack!!.onFailure(Throwable(response.raw().header(ERROR_PARAMETER)))
                    }
                } else {
                    callBack!!.onFailure(Throwable(UNSUCCESSFUL_REQUEST))
                }
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                callBack!!.onFailure(Throwable(SERVER_UNAVAILABLE))
            }
        })
    }

    fun attachPresenter(callBack: CallBackAuthorization) {
        this.callBack = callBack
    }
}
