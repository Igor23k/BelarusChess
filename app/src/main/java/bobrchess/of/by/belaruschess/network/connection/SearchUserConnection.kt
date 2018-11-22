package bobrchess.of.by.belaruschess.network.connection

import org.apache.commons.httpclient.HttpStatus

import bobrchess.of.by.belaruschess.App
import bobrchess.of.by.belaruschess.dto.UserDTO
import bobrchess.of.by.belaruschess.presenter.callback.CallBackSearchUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import bobrchess.of.by.belaruschess.util.Constants.Companion.ERROR_PARAMETER
import bobrchess.of.by.belaruschess.util.Constants.Companion.SERVER_UNAVAILABLE
import bobrchess.of.by.belaruschess.util.Constants.Companion.UNSUCCESSFUL_REQUEST

/**
 * Created by Igor on 11.04.2018.
 */

class SearchUserConnection {

    private var callBack: CallBackSearchUser? = null

    fun getUsers() {
        App.getAPI().users.enqueue(object : Callback<List<UserDTO>> {
            override fun onResponse(call: Call<List<UserDTO>>, response: Response<List<UserDTO>>) {
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

            override fun onFailure(call: Call<List<UserDTO>>, t: Throwable) {
                callBack!!.onFailure(Throwable(SERVER_UNAVAILABLE))
            }
        })
    }

    fun getUsers(count: Int?) {
        App.getAPI().getUsers(count!!).enqueue(object : Callback<List<UserDTO>> {
            override fun onResponse(call: Call<List<UserDTO>>, response: Response<List<UserDTO>>) {
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

            override fun onFailure(call: Call<List<UserDTO>>, t: Throwable) {
                callBack!!.onFailure(t)
            }
        })
    }

    fun searchUsers(text: String) {
        App.getAPI().searchUsers(text).enqueue(object : Callback<List<UserDTO>> {
            override fun onResponse(call: Call<List<UserDTO>>, response: Response<List<UserDTO>>) {
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

            override fun onFailure(call: Call<List<UserDTO>>, t: Throwable) {
                callBack!!.onFailure(t)
            }
        })
    }

    fun attachPresenter(callBack: CallBackSearchUser) {
        this.callBack = callBack
    }
}
