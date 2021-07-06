package bobrchess.of.by.belaruschess.network.connection.internal

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.UserContextDTO
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackAuthorization
import bobrchess.of.by.belaruschess.util.Util
import bobrchess.of.by.belaruschess.util.Util.Companion.buildErrorDto
import org.apache.commons.httpclient.HttpStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthorizationConnection {

    private var callBack: CallBackAuthorization? = null

    fun authorization(userDTO: UserDTO) {
        App.getPersonalServerApi().authorization(userDTO).enqueue(object : Callback<UserContextDTO> {
            override fun onResponse(call: Call<UserContextDTO>, response: Response<UserContextDTO>) {
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

            override fun onFailure(call: Call<UserContextDTO>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun authorization(authorizationHeader: String) {
        App.getPersonalServerApi().authorization(authorizationHeader).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
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

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                callBack!!.onFailure(Util.buildOnFailureResponse())
            }
        })
    }

    fun attachPresenter(callBack: CallBackAuthorization) {
        this.callBack = callBack
    }
}
